package me.theclashfruit.kotrinth.v2

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import me.theclashfruit.kotrinth.enums.Method
import me.theclashfruit.kotrinth.exceptions.ApiException
import me.theclashfruit.kotrinth.enums.Sort
import me.theclashfruit.kotrinth.utils.ApiError
import me.theclashfruit.kotrinth.v2.serializables.Project
import me.theclashfruit.kotrinth.v2.serializables.Search
import me.theclashfruit.kotrinth.v2.serializables.User
import org.jetbrains.annotations.ApiStatus.Experimental
import org.jetbrains.annotations.ApiStatus.Internal
import kotlin.io.use

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

    private val client: HttpClient;

    var rateLimit: Int? = null
    var rateLimitRemaining: Int? = null
    var rateLimitReset: Int? = null

    init {
        if (customUserAgent != null) {
            this.client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }

                install(UserAgent) {
                    agent = customUserAgent
                }
            }
        } else {
            this.client = HttpClient(CIO) {
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
     * @param index Sort by.
     * @param offset The offset.
     * @param limit The limit.
     *
     * @return [me.theclashfruit.kotrinth.v2.serializables.Search]
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun search(query: String? = null, index: Sort? = null, offset: Int = 0, limit: Int = 10): Search {
        val response: HttpResponse = client.get("$modrinthUrl/search") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }

            parameters {
                append("offset", offset.toString())
                append("limit", limit.toString())

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
     * Get the projects of a user by username or id.
     *
     * @param id|username The id of the user.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.serializables.Project] or null if user not found.
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
     * Custom request.
     *
     * Example:
     * `
     * val response = kotrinth.customRequest(HttpMethod.Get, "/projects/fabric-api")
     * `
     *
     * @param method The method to use.
     * @param path The path on the Modrinth api to request.
     * @param body The body to send.
     *
     * @return [io.ktor.client.statement.HttpResponse]
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