package dev.etino.fcshared.featuresKotlin.home.repository

import dev.etino.fcshared.featuresKotlin.home.models.WeatherDisplay
import dev.etino.fcshared.featuresKotlin.home.models.WeatherFeature
import dev.etino.fcshared.featuresKotlin.home.services.WeatherServiceInterface
import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

class WeatherRepository(private val weatherNetworkService: WeatherServiceInterface) : WeatherRepositoryInterface {

    override suspend fun fetchWeatherDetails(): WeatherDisplay? {
        return when (val result = weatherNetworkService.fetchWeatherDetails()) {
            is NetworkServiceResult.WeatherResult.Success -> {
                val weather = json.decodeFromString<WeatherFeature>(result.data)
                val airTemperature =
                    weather.properties?.timeseries?.firstOrNull()
                        ?.data?.instant?.details?.airTemperature
                val summary =
                    weather.properties?.timeseries?.firstOrNull()
                        ?.data?.next1Hours?.summary?.symbolCode?.split("_")
                        ?.firstOrNull()

                if (airTemperature == null || summary == null) {
                    return null
                }

                return WeatherDisplay(
                    "Split",
                    airTemperature,
                    summary
                )
            }

            is NetworkServiceResult.WeatherResult.Failure -> {
                null
            }
        }
    }
}