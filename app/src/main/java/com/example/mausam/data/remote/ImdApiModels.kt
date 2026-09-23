package com.example.mausam.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTO for IMD Current Weather observation endpoint (`/current_wx`).
 */
data class ImdCurrentWxResponse(
    @SerializedName("Station Id") val stationId: String? = null,
    @SerializedName("Station") val stationName: String? = null,
    @SerializedName("Date of Observation") val dateOfObservation: String? = null,
    @SerializedName("Time of Observation UTC") val timeUtc: String? = null,
    @SerializedName("Temperature deg C") val tempC: Double? = null,
    @SerializedName("Humidity %") val humidity: Double? = null,
    @SerializedName("Wind Speed KMPH") val windSpeedKmh: Double? = null,
    @SerializedName("Wind Direction Code") val windDirection: String? = null,
    @SerializedName("M.S.L.P") val mslp: Double? = null,
    @SerializedName("Weather Code") val weatherCode: String? = null,
    @SerializedName("Last 24 hrs Rainfall mm") val rainfall24h: Double? = null
)

/**
 * DTO for IMD Automatic Weather Station endpoint (`/aws_data`).
 */
data class ImdAwsDataResponse(
    @SerializedName("ID") val id: String? = null,
    @SerializedName("STATION") val station: String? = null,
    @SerializedName("DISTRICT") val district: String? = null,
    @SerializedName("STATE") val state: String? = null,
    @SerializedName("CURR_TEMP") val currTemp: String? = null,
    @SerializedName("MIN_TEMP") val minTemp: String? = null,
    @SerializedName("MAX_TEMP") val maxTemp: String? = null,
    @SerializedName("Feel Like") val feelLike: String? = null,
    @SerializedName("RH") val rh: String? = null,
    @SerializedName("WIND_SPEED") val windSpeed: String? = null,
    @SerializedName("WIND_DIRECTION") val windDirection: String? = null,
    @SerializedName("WEATHER_CODE") val weatherCode: String? = null
)

/**
 * DTO for IMD City Weather Forecast endpoint (`/cityforecast` & `/cityforecastloc`).
 */
data class ImdCityForecastResponse(
    @SerializedName("Station_Code") val stationCode: String? = null,
    @SerializedName("Station_Name") val stationName: String? = null,
    @SerializedName("Today_Max_temp") val todayMaxTemp: String? = null,
    @SerializedName("Today_Min_temp") val todayMinTemp: String? = null,
    @SerializedName("Todays_Forecast_Max_Temp") val forecastMaxTemp: String? = null,
    @SerializedName("Todays_Forecast_Min_temp") val forecastMinTemp: String? = null,
    @SerializedName("Todays_Forecast") val todaysForecast: String? = null,
    @SerializedName("Past_24_hrs_Rainfall") val rainfall24h: String? = null,
    @SerializedName("Relative_Humidity_at_0830") val rh0830: String? = null,
    @SerializedName("Relative_Humidity_at_1730") val rh1730: String? = null,
    @SerializedName("Sunrise_time") val sunriseTime: String? = null,
    @SerializedName("Sunset_time") val sunsetTime: String? = null
)

/**
 * DTO for IMD Astronomical Sun and Moon endpoint (`/sunmoon`).
 */
data class ImdSunMoonResponse(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<ImdSunMoonData>? = null
)

data class ImdSunMoonData(
    @SerializedName("sunrise") val sunrise: String? = null,
    @SerializedName("sunset") val sunset: String? = null,
    @SerializedName("moonrise") val moonrise: String? = null,
    @SerializedName("moonset") val moonset: String? = null
)
