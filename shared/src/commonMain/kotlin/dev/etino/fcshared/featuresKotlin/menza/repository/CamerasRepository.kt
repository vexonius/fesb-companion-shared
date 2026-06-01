package dev.etino.fcshared.featuresKotlin.menza.repository

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.featuresKotlin.menza.CamerasResult
import dev.etino.fcshared.featuresKotlin.menza.service.CamerasServiceInterface
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url

class CamerasRepository(private val camerasService: CamerasServiceInterface) : CamerasRepositoryInterface {

    override suspend fun getImages(path: String): Url? {
        return when (val result = camerasService.getCameraImageUrls(path)) {
            is CamerasResult.GetCamerasResult.Success -> {

                val hrefs = parseImageUrls(result.data)
                if (hrefs.isEmpty()) {
                    return null
                }
                URLBuilder(
                    protocol = URLProtocol.HTTPS,
                    host = "camerasfiles.dbtouch.com",
                    pathSegments = listOf("images", path, hrefs.last())
                ).build()
            }

            is CamerasResult.GetCamerasResult.Failure -> {
                throw Exception("Images urls fetching error")
            }
        }
    }

    private fun parseImageUrls(body: String): List<String> {
        return Ksoup.parse(body).select("a")
            .map { it.attr("href") }
            .filter { !it.contains("medium") }
            .filter { !it.contains("small") }
            .filter { !it.contains("../") }
    }
}