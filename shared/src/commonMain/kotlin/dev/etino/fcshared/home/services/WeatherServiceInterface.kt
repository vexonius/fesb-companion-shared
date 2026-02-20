package dev.etino.fcshared.home.services

import dev.etino.fcshared.networking.NetworkServiceResult

interface WeatherServiceInterface {

    suspend fun fetchWeatherDetails(): NetworkServiceResult.WeatherResult
}