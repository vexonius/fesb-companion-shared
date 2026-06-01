package dev.etino.fcshared.featuresKotlin.home.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface WeatherServiceInterface {

    suspend fun fetchWeatherDetails(): NetworkServiceResult.WeatherResult
}