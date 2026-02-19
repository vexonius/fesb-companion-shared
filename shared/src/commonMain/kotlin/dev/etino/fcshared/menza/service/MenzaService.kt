package dev.etino.fcshared.menza.service

import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.path

class MenzaService(private val client: HttpClient) : MenzaServiceInterface {

    override suspend fun fetchMenza(place: String): NetworkServiceResult.MenzaResult {
        val response: HttpResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "sc.dbtouch.com"
                path("menu", "api.php")
                parameters.append("place", place)
            }
        }

        val data = response.bodyAsText()
        return if (response.status.isSuccess() && data.isNotEmpty()) {
            NetworkServiceResult.MenzaResult.Success(data)
        } else {
            NetworkServiceResult.MenzaResult.Failure(Throwable("Failed to fetch menza details."))
        }
    }
}