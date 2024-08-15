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
import me.theclashfruit.kotrinth.exceptions.ApiException
import me.theclashfruit.kotrinth.enums.Sort
import me.theclashfruit.kotrinth.utils.ApiError
import me.theclashfruit.kotrinth.v2.serializables.Search

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
    private val modrinthUrl = "https://api.modrinth.com/v2/"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    var rateLimit: Int? = null
    var rateLimitRemaining: Int? = null
    var rateLimitReset: Int? = null

    init {
        if (customUserAgent != null) {
            client.config {
                install(UserAgent) {
                    agent = customUserAgent
                }
            }
        } else {
            client.config {
                install(UserAgent) {
                    agent = "$appName/$appVersion ($appContact) Kotrinth/1.0.0"
                }
            }
        }
    }

    /**
     * Search for projects on Modrinth.
     *
     * @param query The query to search for.
     * @param index Sort by.
     * @param offset The offset.
     * @param limit The limit.
     *
     * @return [me.theclashfruit.kotrinth.data.Search]
     * @throws [me.theclashfruit.kotrinth.ApiException]
     */
    suspend fun search(query: String? = null, index: Sort? = null, offset: Int = 0, limit: Int = 10): Search {
        val response: HttpResponse = client.get(modrinthUrl + "search") {
            parameters {
                append("offset", offset.toString())
                append("limit", limit.toString())

                if (query != null) {
                    append("query", query)
                }

                if (index != null) {
                    append("index", index.value)
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

    private fun setRateLimits(response: HttpResponse) {
        rateLimit = response.headers.get("X-RateLimit-Limit")?.toInt()
        rateLimitRemaining = response.headers.get("X-RateLimit-Remaining")?.toInt()
        rateLimitReset = response.headers.get("X-RateLimit-Reset")?.toInt()
    }
}