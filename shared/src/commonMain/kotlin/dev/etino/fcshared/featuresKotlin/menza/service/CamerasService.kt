package dev.etino.fcshared.featuresKotlin.menza.service

import dev.etino.fcshared.featuresKotlin.menza.CamerasResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.path

class CamerasService(private val client: HttpClient) : CamerasServiceInterface {

    override suspend fun getCameraImageUrls(path: String): CamerasResult.GetCamerasResult {

        val response: HttpResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "camerasfiles.dbtouch.com"
                path("images", "$path/")
            }
        }

        if (!response.status.isSuccess()) {
            return CamerasResult.GetCamerasResult.Failure
        }

        val doc = response.bodyAsText()
        return CamerasResult.GetCamerasResult.Success(doc)
    }
}