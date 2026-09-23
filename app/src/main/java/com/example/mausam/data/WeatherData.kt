package com.example.mausam.data

import com.example.mausam.ui.WeatherIconType

/**
 * Air Quality metric model (ready for IMD / CPCB API responses).
 */
data class AirQualityData(
    val pollutantName: String = "PM 2.5",
    val value: Int = 160
)

/**
 * Hourly weather forecast item model.
 */
data class HourlyForecastItem(
    val time: String,               // e.g. "12:00", "13:00"
    val tempC: Int,                 // e.g. 32, 34
    val popPercent: Int,            // Probability of Precipitation (0 to 100)
    val windSpeedKmh: Int,          // e.g. 5, 14, 28, 62, 85
    val windDirection: String,      // e.g. "N", "NE", "SE", "SW", "W"
    val iconType: WeatherIconType,  // Weather icon enum type
    val isSelected: Boolean = false // Whether this hour slot is currently selected
)

/**
 * Complete weather dashboard data model (API-ready for IMD service calls).
 */
data class WeatherData(
    val locationName: String = "Kharar",
    val conditionText: String = "Clear Night",
    val tempC: Int = 22,
    val minTempC: Int = 22,
    val maxTempC: Int = 34,
    val feelsLikeC: Int = 21,
    val isDaytime: Boolean = false,
    val airQuality: AirQualityData = AirQualityData(),
    val hourlyForecasts: List<HourlyForecastItem> = listOf(
        HourlyForecastItem("12:00", 32, 0, 5, "N", WeatherIconType.PARTLY_CLOUDY, false),
        HourlyForecastItem("13:00", 32, 10, 14, "NE", WeatherIconType.LIGHT_RAIN, true),
        HourlyForecastItem("14:00", 33, 40, 28, "SE", WeatherIconType.MODERATE_RAIN, false),
        HourlyForecastItem("15:00", 34, 60, 62, "SW", WeatherIconType.THUNDERSTORM, false),
        HourlyForecastItem("16:00", 33, 70, 85, "W", WeatherIconType.HEAVY_RAIN, false)
    )
)

/**
 * Repository / Data Provider for weather data.
 */
object MockWeatherRepository {
    fun getWeatherData(): WeatherData {
        return WeatherData()
    }
}
