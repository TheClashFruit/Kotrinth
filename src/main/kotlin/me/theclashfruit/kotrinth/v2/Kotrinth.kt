package me.theclashfruit.kotrinth.v2

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import me.theclashfruit.kotrinth.enums.Method
import me.theclashfruit.kotrinth.enums.ProjectType
import me.theclashfruit.kotrinth.enums.Side
import me.theclashfruit.kotrinth.enums.Sort
import me.theclashfruit.kotrinth.exceptions.ApiException
import me.theclashfruit.kotrinth.utils.AndroidUtil.isRunningOnAndroid
import me.theclashfruit.kotrinth.utils.ApiError
import me.theclashfruit.kotrinth.v2.serializables.Project
import me.theclashfruit.kotrinth.v2.serializables.Search
import me.theclashfruit.kotrinth.v2.serializables.User
import me.theclashfruit.kotrinth.v2.serializables.tags.CategoryTag
import me.theclashfruit.kotrinth.v2.serializables.tags.GameVersionTag
import me.theclashfruit.kotrinth.v2.serializables.tags.GenericTag
import me.theclashfruit.kotrinth.v2.serializables.tags.LoaderTag
import org.jetbrains.annotations.ApiStatus.Experimental
import org.jetbrains.annotations.ApiStatus.Internal

/**
 * A Kotlin wrapper for the Modrinth API. (Modrinth API v2)
 *
 * Default user-agent: `$appName/$appVersion ($appContact) Kotrinth/$kotrinthVersion`
 *
 * @param appName The name of the application.
 * @param appVersion The version of the application.
 * @param appContact The contact information of the application. Either an email or the source url or both.
 *
 * @param customUserAgent Custom user-agent to override the default one.
 */
class Kotrinth(appName: String, appVersion: String, appContact: String, customUserAgent: String? = null) {
    private val modrinthUrl = "https://api.modrinth.com/v2"
    private var token: String? = null;

    internal val client: HttpClient;

    var rateLimit: Int? = null
    var rateLimitRemaining: Int? = null
    var rateLimitReset: Int? = null

    /**
     * Modrinth Tags.
     */
    val tag = Tags()

    init {
        if (customUserAgent != null) {
            this.client = HttpClient(if (isRunningOnAndroid()) Android else CIO) {
                install(ContentNegotiation) {
                    json()
                }

                install(UserAgent) {
                    agent = customUserAgent
                }
            }
        } else {
            this.client = HttpClient(if (isRunningOnAndroid()) Android else CIO) {
                install(ContentNegotiation) {
                    json()
                }

                install(UserAgent) {
                    agent = "$appName/$appVersion ($appContact) Kotrinth/1.0.0"
                }
            }
        }
    }

    /**
     * Authorize the client with a token.
     *
     * @param token The token to authorize with.
     */
    fun authorize(token: String) {
        this.token = token
    }

    /**
     * Search for projects on Modrinth.
     *
     * @param query The query to search for.
     * @param facets The facets to filter by.
     * @param index Sort by.
     * @param offset The offset.
     * @param limit The limit.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.Search]
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun search(query: String? = null, facets: List<List<String>>? = null, index: Sort? = null, offset: Int = 0, limit: Int = 10): Search {
        val response: HttpResponse = client.get("$modrinthUrl/search") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }

            parameters {
                append("offset", offset.toString())
                append("limit", limit.toString())

                if (facets != null) {
                    append("facets", facets.joinToString(
                        prefix = "[",
                        separator = ",",
                        postfix = "]"
                    ) {
                        it.joinToString(
                            prefix = "[\"",
                            separator = "\",\"",
                            postfix = "\"]"
                        )
                    })
                }

                if (query != null) {
                    append("query", query)
                }

                if (index != null) {
                    append("index", index.toString())
                }
            }
        }

        setRateLimits(response)

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: Search = response.body()

        return res
    }

    /**
     * Get a project by slug or id.
     *
     * @param id|slug The id of the project.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.Project] or null if project not found.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun project(id: String): Project? {
        val response: HttpResponse = client.get("$modrinthUrl/project/$id") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: Project = response.body()

        return res
    }

    /**
     * Get the authenticated user.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.User] or null if user not authenticated.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun user(): User? {
        val response: HttpResponse = client.get("$modrinthUrl/user") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        return parseUser(response)
    }

    /**
     * Get a user by username or id.
     *
     * @param id|username The id of the user.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.User] or null if user not found.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun user(id: String): User? {
        val response: HttpResponse = client.get("$modrinthUrl/user/$id") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        return parseUser(response)
    }

    /**
     * Get multiple users by username or id.
     *
     * @param id|username The id of the user.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.User] or null if user not found.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun users(vararg id: String): List<User>? {
        val response: HttpResponse = client.get("$modrinthUrl/users") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }

            url {
                parameters.append("ids", "[\"${id.joinToString("\",\"")}\"]")
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: List<User> = response.body()

        return res
    }

    /**
     * Get the projects of the authenticated user.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.serializables.Project] or null if user not authenticated.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun userProjects(): List<Project>? {
        val user = user() ?: return null

        val response: HttpResponse = client.get("$modrinthUrl/user/${user.id}/projects") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: List<Project> = response.body()

        return res
    }

    /**
     * Get the projects of a user by username or id.
     *
     * @param id|username The id of the user.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.serializables.Project] or null if user not found.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun userProjects(id: String): List<Project>? {
        val response: HttpResponse = client.get("$modrinthUrl/user/$id/projects") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: List<Project> = response.body()

        return res
    }

    /**
     * Get the projects the authenticated user is following.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.serializables.Project] or null if user is not authorized.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun userFollowedProjects(): List<Project>? {
        val user = user() ?: return null

        val response: HttpResponse = client.get("$modrinthUrl/user/${user.id}/follows") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null
        if (response.status == HttpStatusCode.Unauthorized) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: List<Project> = response.body()

        return res
    }

    /**
     * Get the projects a user is following by username or id.
     *
     * @param id|username The id of the user.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.serializables.Project] or null if user is not authorized.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun userFollowedProjects(id: String): List<Project>? {
        val response: HttpResponse = client.get("$modrinthUrl/user/$id/follows") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        setRateLimits(response)

        if (response.status == HttpStatusCode.NotFound) return null
        if (response.status == HttpStatusCode.Unauthorized) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: List<Project> = response.body()

        return res
    }


    inner class Tags {
        /**
         * Get a list of categories.
         *
         * @return A list of [me.theclashfruit.kotrinth.v2.serializables.tags.CategoryTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun category(): List<CategoryTag> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/category")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<CategoryTag> = response.body()

            return res
        }

        /**
         * Get a list of loaders.
         *
         * @return A list of [me.theclashfruit.kotrinth.v2.serializables.tags.LoaderTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun loader(): List<LoaderTag> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/loader")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<LoaderTag> = response.body()

            return res
        }

        /**
         * Get a list of game versions.
         *
         * @return A list of [me.theclashfruit.kotrinth.v2.serializables.tags.GameVersionTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun gameVersion(): List<GameVersionTag> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/game_version")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<GameVersionTag> = response.body()

            return res
        }

        /**
         * Get a list of licenses.
         *
         * @return A list of [me.theclashfruit.kotrinth.v2.serializables.tags.GenericTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        @Deprecated("Simply use SPDX IDs.")
        suspend fun license(): List<GenericTag> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/license")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<GenericTag> = response.body()

            return res
        }

        /**
         * Get a specific license by ID.
         *
         * @param id The ID of the license.
         *
         * @return [me.theclashfruit.kotrinth.v2.serializables.tags.GenericTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun license(id: String): GenericTag {
            val response: HttpResponse = client.get("$modrinthUrl/tag/license/$id")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: GenericTag = response.body()

            return res
        }

        /**
         * Get a list of donation platforms.
         *
         * @return A list of [me.theclashfruit.kotrinth.v2.serializables.tags.GenericTag]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun donationPlatform(): List<GenericTag> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/donation_platform")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<GenericTag> = response.body()

            return res
        }

        /**
         * Get a list of report types.
         *
         * @return A list of [String]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun reportType(): List<String> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/report_type")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<String> = response.body()

            return res
        }

        /**
         * Get a list of project types.
         *
         * @return A list of [me.theclashfruit.kotrinth.enums.ProjectType]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun projectType(): List<ProjectType> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/project_type")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<ProjectType> = response.body()

            return res
        }

        /**
         * Get a list of sides.
         *
         * @return A list of [me.theclashfruit.kotrinth.enums.Side]
         * @throws [me.theclashfruit.kotrinth.utils.ApiError]
         */
        suspend fun sideType(): List<Side> {
            val response: HttpResponse = client.get("$modrinthUrl/tag/side")

            setRateLimits(response)

            if (response.status != HttpStatusCode.OK) {
                val res: ApiError = response.body()

                throw ApiException(res)
            }

            val res: List<Side> = response.body()

            return res
        }
    }

    /**
     * Custom request.
     *
     * Example:
     * `
     * val response = kotrinth.customRequest(Method.Get, "/projects/fabric-api")
     * `
     *
     * @param method The method to use.
     * @param path The path on the Modrinth api to request.
     * @param body The body to send.
     *
     * @return [io.ktor.client.statement.HttpResponse]
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    @Experimental
    @OptIn(InternalAPI::class)
    suspend fun customRequest(method: Method, path: String, body: String? = null): HttpResponse {
        val response: HttpResponse = client.request("$modrinthUrl$path") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }

            this.method = method.value

            if (body != null) {
                this.body = body
            }
        }

        setRateLimits(response)

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        return response
    }

    /**
     * Test the configuration of the client.
     *
     * For testing if client is configured correctly.
     */
    @Internal
    suspend fun testConfig() {
        val response: HttpResponse = client.get("http://localhost:3000/")
        val res: String = response.body()

        println(res)
    }

    private suspend fun parseUser(response: HttpResponse): User? {
        setRateLimits(response)

        if (response.status == HttpStatusCode.Unauthorized) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        val res: User = response.body()

        return res
    }

    private fun setRateLimits(response: HttpResponse) {
        rateLimit = response.headers.get("X-RateLimit-Limit")?.toInt()
        rateLimitRemaining = response.headers.get("X-RateLimit-Remaining")?.toInt()
        rateLimitReset = response.headers.get("X-RateLimit-Reset")?.toInt()
    }
}