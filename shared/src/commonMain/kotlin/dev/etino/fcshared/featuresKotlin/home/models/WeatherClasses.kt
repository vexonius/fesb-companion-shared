package dev.etino.fcshared.featuresKotlin.home.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherFeature(
    val type: String?,
    val geometry: Geometry?,
    val properties: Properties?
)

@Serializable
data class Geometry(
    val type: String?,
    val coordinates: List<Double>?
)

@Serializable
data class Properties(
    val meta: Meta?,
    val timeseries: List<TimeSeries>?
)

@Serializable
data class Meta(
    val units: Units?,
    @SerialName("updated_at") val updatedAt: String?
)

@Serializable
data class Units(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String?,
    @SerialName("air_temperature")
    val airTemperature: String?,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: String?,
    @SerialName("precipitation_amount")
    val precipitationAmount: String?,
    @SerialName("relative_humidity")
    val relativeHumidity: String?,
    @SerialName("wind_from_direction")
    val windFromDirection: String?,
    @SerialName("wind_speed")
    val windSpeed: String?,
)

@Serializable
data class TimeSeries(
    val time: String?,
    val data: Data?
)

@Serializable
data class Data(
    val instant: Instant?,
    @SerialName("next_12_hours")
    val next12Hours: Forecast?,
    @SerialName("next_1_hours")
    val next1Hours: Forecast?,
    @SerialName("next_6_hours")
    val next6Hours: Forecast?
)

@Serializable
data class Instant(
    val details: InstantDetails?
)

@Serializable
data class InstantDetails(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double?,
    @SerialName("air_temperature")
    val airTemperature: Double?,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: Double?,
    @SerialName("relative_humidity")
    val relativeHumidity: Double?,
    @SerialName("wind_from_direction")
    val windFromDirection: Double?,
    @SerialName("wind_speed")
    val windSpeed: Double?,
)

@Serializable
data class Forecast(
    val details: ForecastDetails?,
    val summary: Summary?
)

@Serializable
data class ForecastDetails(
    @SerialName("air_temperature_max")
    val airTemperatureMax: Double?,
    @SerialName("air_temperature_min")
    val airTemperatureMin: Double?,
    @SerialName("precipitation_amount")
    val precipitationAmount: Double?,
    @SerialName("precipitation_amount_max")
    val precipitationAmountMax: Double?,
    @SerialName("precipitation_amount_min")
    val precipitationAmountMin: Double?,
    @SerialName("probability_of_precipitation")
    val probabilityOfPrecipitation: Double?,
    @SerialName("probability_of_thunder")
    val probabilityOfThunder: Double?,
    @SerialName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Double?
)

@Serializable
data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String?
)

data class WeatherDisplay(
    val location: String,
    val temperature: Double,
    val summary: String
)