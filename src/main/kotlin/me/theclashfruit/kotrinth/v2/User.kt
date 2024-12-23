package me.theclashfruit.kotrinth.v2

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.headers
import io.ktor.client.statement.*
import io.ktor.http.*
import me.theclashfruit.kotrinth.Kotrinth
import me.theclashfruit.kotrinth.exceptions.ApiException
import me.theclashfruit.kotrinth.utils.ApiError
import me.theclashfruit.kotrinth.v2.serializables.UserData
import me.theclashfruit.kotrinth.v2.serializables.mod.UserModData

class User(
    private val data: UserData,
    private val kotrinth: Kotrinth
) {
    val badges get() = data.badges
    val bio get() = data.bio
    val created get() = data.created
    val id get() = data.id
    val role get() = data.role
    val username get() = data.username
    val email get() = data.email
    val name get() = data.name
    val hasPassword get() = data.hasPassword
    val hasTotp get() = data.hasTotp
    val payoutData get() = data.payoutData
    val emailVerified get() = data.emailVerified
    val authProviders get() = data.authProviders
    val avatarUrl get() = data.avatarUrl

    private val modrinthUrl = kotrinth.modrinthUrl
    private val client = kotrinth.client

    private val token = kotrinth.token

    /**
     * Update the user's data.
     *
     * @param configure The data to update.
     *
     * @return True if the update was successful, false otherwise.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun update(configure: UserModData.() -> Unit): Boolean {
        val updateData = UserModData(
            username = data.username
        ).apply(configure)

        val response: HttpResponse = client.patch("$modrinthUrl/v2/user/$id") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }

            contentType(ContentType.Application.Json)
            setBody(updateData)
        }

        kotrinth.setRateLimits(response)

        if (
            response.status != HttpStatusCode.NoContent ||
            response.status != HttpStatusCode.Unauthorized
        ) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        return (response.status == HttpStatusCode.NoContent)
    }

    /**
     * Get the user's projects.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.Project] or null.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun projects(): List<Project>? {
        val response: HttpResponse = client.get("$modrinthUrl/v2/user/$id/projects") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        kotrinth.setRateLimits(response)
        return kotrinth.parseProjects(response)
    }

    /**
     * Get the user's followed projects.
     *
     * @return A list of [me.theclashfruit.kotrinth.v2.Project] or null if user is not authorized.
     * @throws [me.theclashfruit.kotrinth.utils.ApiError]
     */
    suspend fun follows(): List<Project>? {
        val response: HttpResponse = client.get("$modrinthUrl/v2/user/$id/follows") {
            headers {
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }

        kotrinth.setRateLimits(response)
        return kotrinth.parseProjects(response)
    }
}