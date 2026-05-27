package dev.etino.fcshared.features.home.utils

import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.weather_clearsky
import fesb_companion_shared.composeapp.generated.resources.weather_cloudy
import fesb_companion_shared.composeapp.generated.resources.weather_fair
import fesb_companion_shared.composeapp.generated.resources.weather_fog
import fesb_companion_shared.composeapp.generated.resources.weather_heavyrain
import fesb_companion_shared.composeapp.generated.resources.weather_heavyrainandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_heavyrainshowers
import fesb_companion_shared.composeapp.generated.resources.weather_heavyrainshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_heavysleet
import fesb_companion_shared.composeapp.generated.resources.weather_heavysleetandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_heavysleetshowers
import fesb_companion_shared.composeapp.generated.resources.weather_heavysleetshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_heavysnow
import fesb_companion_shared.composeapp.generated.resources.weather_heavysnowandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_heavysnowshowers
import fesb_companion_shared.composeapp.generated.resources.weather_heavysnowshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightrain
import fesb_companion_shared.composeapp.generated.resources.weather_lightrainandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightrainshowers
import fesb_companion_shared.composeapp.generated.resources.weather_lightrainshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightsleet
import fesb_companion_shared.composeapp.generated.resources.weather_lightsleetandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightsleetshowers
import fesb_companion_shared.composeapp.generated.resources.weather_lightsnow
import fesb_companion_shared.composeapp.generated.resources.weather_lightsnowandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightsnowshowers
import fesb_companion_shared.composeapp.generated.resources.weather_lightssleetshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_lightssnowshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_partlycloudy
import fesb_companion_shared.composeapp.generated.resources.weather_rain
import fesb_companion_shared.composeapp.generated.resources.weather_rainandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_rainshowers
import fesb_companion_shared.composeapp.generated.resources.weather_rainshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_sleet
import fesb_companion_shared.composeapp.generated.resources.weather_sleetandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_sleetshowers
import fesb_companion_shared.composeapp.generated.resources.weather_sleetshowersandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_snow
import fesb_companion_shared.composeapp.generated.resources.weather_snowandthunder
import fesb_companion_shared.composeapp.generated.resources.weather_snowshowers
import fesb_companion_shared.composeapp.generated.resources.weather_snowshowersandthunder
import org.jetbrains.compose.resources.StringResource


fun getWeatherText(key: String?): StringResource {
    return when (key) {
            "clearsky" -> Res.string.weather_clearsky
            "fair" -> Res.string.weather_fair
            "partlycloudy" -> Res.string.weather_partlycloudy
            "cloudy" -> Res.string.weather_cloudy
            "rainshowers" -> Res.string.weather_rainshowers
            "rainshowersandthunder" -> Res.string.weather_rainshowersandthunder
            "sleetshowers" -> Res.string.weather_sleetshowers
            "snowshowers" -> Res.string.weather_snowshowers
            "rain" -> Res.string.weather_rain
            "heavyrain" -> Res.string.weather_heavyrain
            "heavyrainandthunder" -> Res.string.weather_heavyrainandthunder
            "sleet" -> Res.string.weather_sleet
            "snow" -> Res.string.weather_snow
            "snowandthunder" -> Res.string.weather_snowandthunder
            "fog" -> Res.string.weather_fog
            "sleetshowersandthunder" -> Res.string.weather_sleetshowersandthunder
            "snowshowersandthunder" -> Res.string.weather_snowshowersandthunder
            "rainandthunder" -> Res.string.weather_rainandthunder
            "sleetandthunder" -> Res.string.weather_sleetandthunder
            "lightrainshowersandthunder" -> Res.string.weather_lightrainshowersandthunder
            "heavyrainshowersandthunder" -> Res.string.weather_heavyrainshowersandthunder
            "lightssleetshowersandthunder" -> Res.string.weather_lightssleetshowersandthunder
            "heavysleetshowersandthunder" -> Res.string.weather_heavysleetshowersandthunder
            "lightssnowshowersandthunder" -> Res.string.weather_lightssnowshowersandthunder
            "heavysnowshowersandthunder" -> Res.string.weather_heavysnowshowersandthunder
            "lightrainandthunder" -> Res.string.weather_lightrainandthunder
            "lightsleetandthunder" -> Res.string.weather_lightsleetandthunder
            "heavysleetandthunder" -> Res.string.weather_heavysleetandthunder
            "lightsnowandthunder" -> Res.string.weather_lightsnowandthunder
            "heavysnowandthunder" -> Res.string.weather_heavysnowandthunder
            "lightrainshowers" -> Res.string.weather_lightrainshowers
            "heavyrainshowers" -> Res.string.weather_heavyrainshowers
            "lightsleetshowers" -> Res.string.weather_lightsleetshowers
            "heavysleetshowers" -> Res.string.weather_heavysleetshowers
            "lightsnowshowers" -> Res.string.weather_lightsnowshowers
            "heavysnowshowers" -> Res.string.weather_heavysnowshowers
            "lightrain" -> Res.string.weather_lightrain
            "lightsleet" -> Res.string.weather_lightsleet
            "heavysleet" -> Res.string.weather_heavysleet
            "lightsnow" -> Res.string.weather_lightsnow
            "heavysnow" -> Res.string.weather_heavysnow
            else -> Res.string.weather_clearsky // fallback
        }
}