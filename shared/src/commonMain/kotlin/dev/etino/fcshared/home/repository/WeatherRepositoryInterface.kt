package dev.etino.fcshared.home.repository

import dev.etino.fcshared.home.models.WeatherDisplay

interface WeatherRepositoryInterface {

    suspend fun fetchWeatherDetails(): WeatherDisplay?

}