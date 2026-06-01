package dev.etino.fcshared.featuresKotlin.home.repository

import dev.etino.fcshared.featuresKotlin.home.models.WeatherDisplay

interface WeatherRepositoryInterface {

    suspend fun fetchWeatherDetails(): WeatherDisplay?

}