package dev.etino.fcshared.featuresKotlin.home.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

class WeatherService(private val client: HttpClient) : WeatherServiceInterface {

    override suspend fun fetchWeatherDetails(): NetworkServiceResult.WeatherResult {
        val response: HttpResponse = client.get("https://api.met.no/weatherapi/locationforecast/2.0/compact") {
            url {
                parameters.append("lat", "43.511287")
                parameters.append("lon", "16.469252")
            }
            header("Accept", "application/xml")
            header("User-Agent", "FesbCompanion/1.0")
        }

        val value: String = response.bodyAsText()

        return if (!response.status.isSuccess() || value.isEmpty()) {
            NetworkServiceResult.WeatherResult.Failure(Throwable("Failed to fetch weather"))
        } else {
            NetworkServiceResult.WeatherResult.Success(value)
        }
    }
}