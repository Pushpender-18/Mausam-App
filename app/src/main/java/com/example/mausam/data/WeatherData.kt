package com.example.mausam.data

/**
 * Air Quality metric model.
 */
data class AirQualityData(
    val pollutantName: String = "PM 2.5",
    val value: Int? = 160
)

/**
 * Hourly weather forecast item model.
 */
data class HourlyForecastItem(
    val time: String,               // e.g. "00:00", "01:00", ..., "23:00"
    val tempC: Int? = null,         // e.g. 22, 34
    val popPercent: Int? = null,    // Probability of Precipitation (0 to 100)
    val windSpeedKmh: Int? = null,  // e.g. 5, 14, 28
    val windDirection: String = "-",// e.g. "N", "NE", "SE"
    val iconType: WeatherIconType = WeatherIconType.PARTLY_CLOUDY,
    val isSelected: Boolean = false
)

/**
 * Complete weather dashboard data model.
 */
data class WeatherData(
    val locationName: String? = "Kharar",
    val conditionText: String? = "Clear Night",
    val tempC: Int? = 22,
    val minTempC: Int? = 22,
    val maxTempC: Int? = 34,
    val feelsLikeC: Int? = 21,
    val isDaytime: Boolean = false,
    val airQuality: AirQualityData = AirQualityData("PM 2.5", 160),
    val hourlyForecasts: List<HourlyForecastItem> = MockWeatherRepository.generate24HourForecasts(),
    val isSuccess: Boolean = true
)

/**
 * Repository / Data Provider for weather data.
 */
object MockWeatherRepository {

    fun generate24HourForecasts(): List<HourlyForecastItem> {
        val temps = intArrayOf(
            21, 20, 20, 19, 19, 20, // 00:00 - 05:00
            21, 23, 25, 27, 29, 31, // 06:00 - 11:00
            32, 33, 34, 34, 33, 32, // 12:00 - 17:00
            30, 28, 26, 24, 23, 22  // 18:00 - 23:00
        )

        val pops = intArrayOf(
            0, 0, 0, 0, 5, 10,
            10, 15, 20, 30, 40, 50,
            60, 70, 65, 50, 40, 30,
            20, 15, 10, 5, 0, 0
        )

        val winds = intArrayOf(
            5, 6, 6, 5, 4, 5,
            8, 10, 12, 14, 18, 22,
            25, 28, 30, 26, 22, 18,
            14, 10, 8, 6, 5, 5
        )

        val dirs = arrayOf(
            "N", "N", "NE", "NE", "E", "E",
            "SE", "SE", "S", "S", "SW", "SW",
            "W", "W", "NW", "NW", "N", "N", "NE", "NE", "E", "E", "SE", "SE"
        )

        val iconTypes = arrayOf(
            WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY,
            WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY,
            WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY, WeatherIconType.LIGHT_RAIN,
            WeatherIconType.LIGHT_RAIN, WeatherIconType.MODERATE_RAIN, WeatherIconType.MODERATE_RAIN,
            WeatherIconType.HEAVY_RAIN, WeatherIconType.THUNDERSTORM, WeatherIconType.THUNDERSTORM,
            WeatherIconType.HEAVY_RAIN, WeatherIconType.MODERATE_RAIN, WeatherIconType.LIGHT_RAIN,
            WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY,
            WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY, WeatherIconType.PARTLY_CLOUDY
        )

        return List(24) { hour ->
            val timeString = String.format("%02d:00", hour)
            HourlyForecastItem(
                time = timeString,
                tempC = temps[hour],
                popPercent = pops[hour],
                windSpeedKmh = winds[hour],
                windDirection = dirs[hour],
                iconType = iconTypes[hour],
                isSelected = false
            )
        }
    }

    fun getWeatherData(): WeatherData {
        return WeatherData()
    }
}
