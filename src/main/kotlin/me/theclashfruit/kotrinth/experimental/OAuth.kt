package me.theclashfruit.kotrinth.experimental

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.theclashfruit.kotrinth.enums.Scope
import me.theclashfruit.kotrinth.exceptions.ApiException
import me.theclashfruit.kotrinth.experimental.serializables.TokenResponse
import me.theclashfruit.kotrinth.utils.ApiError
import me.theclashfruit.kotrinth.v2.Kotrinth
import org.jetbrains.annotations.ApiStatus.Experimental

/**
 * Modrinth OAuth
 *
 * @param kotrinth A Kotrinth instance.
 * @param clientId OAuth application client id.
 * @param clientSecret OAuth application client secret.
 */
@Experimental
class OAuth(kotrinth: Kotrinth, clientId: String, clientSecret: String) {
    private val modrinthUrl = "https://api.modrinth.com/_internal"

    private val clientId = clientId
    private val clientSecret = clientSecret

    private val client = kotrinth.client

    /**
     * Create an authorization url.
     *
     * @param redirectUri The url to redirect back to.
     * @param scopes The requested scopes.
     */
    fun createAuthUrl(
        redirectUri: String,
        scopes: Set<Scope>,
        state: String? = null
    ): String {
        val uri = URLBuilder("https://modrinth.com/auth/authorize")

        uri.parameters.append("client_id", clientId)
        uri.parameters.append("redirect_uri", redirectUri)
        uri.parameters.append("scope", scopes.joinToString(" "))

        if (state != null) {
            uri.parameters.append("state", state)
        }

        return uri.toString()
    }

    /**
     * Get the auth token.
     *
     * @param redirectUri The redirect url that was used.
     * @param code The code returned by the authorization.
     *
     * @return `TokenResponse` or `null`.
     */
    suspend fun token(
        redirectUri: String,
        code: String,
    ): TokenResponse? {
        val response: HttpResponse = client.post("$modrinthUrl/oauth/token") {
            contentType(ContentType.Application.FormUrlEncoded)

            header(HttpHeaders.Authorization, clientSecret)

            setBody(
                FormDataContent(Parameters.build {
                    append("code", code)
                    append("redirect_uri", redirectUri)
                    append("client_id", clientId)
                    append("grant_type", "authorization_code")
                })
            )
        }

        if (response.status == HttpStatusCode.Unauthorized) return null

        if (response.status != HttpStatusCode.OK) {
            val res: ApiError = response.body()

            throw ApiException(res)
        }

        return response.body<TokenResponse>()
    }
}