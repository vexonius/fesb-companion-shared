package dev.etino.fcshared.login.services

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.login.user.models.User
import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.request
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class UserService(val client: HttpClient) : UserServiceInterface {

    override suspend fun loginUser(username: String, password: String): NetworkServiceResult.LoginResult {
        val response = client.submitForm(
            url = loginUrl.toString(),
            formParameters = Parameters.build {
                append("Username", username)
                append("Password", password)
                append("IsRememberMeChecked", "true")
            }
        )
        val url = response.request.url

        val nameOfUser = Ksoup.parse(response.body<String>()).select(".welcomeBack h2").text()

        return if (targetUrl.toString().contains(url.toString())) {
            NetworkServiceResult.LoginResult.Success(User(nameOfUser, username, password))
        } else {
            NetworkServiceResult.LoginResult.Failure(Throwable("Error during login"))
        }
    }

    companion object {
        private val SCHEME = URLProtocol.HTTPS

        val targetUrl = URLBuilder().apply {
            protocol = SCHEME
            host = "korisnik.fesb.unist.hr"
        }.build()

        val loginUrl = URLBuilder().apply {
            protocol = SCHEME
            host = "korisnik.fesb.unist.hr"
            appendPathSegments("prijava")
        }.build()
    }

}