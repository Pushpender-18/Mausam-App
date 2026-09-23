package com.example.mausam.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface defining public REST API endpoints for the India Meteorological Department (IMD).
 * Base URL: https://api.imd.gov.in/api/v1/
 */
interface ImdApiService {

    /**
     * Current Weather Observation by Station ID.
     */
    @GET("current_wx")
    suspend fun getCurrentWx(
        @Query("id") stationId: String = "42182"
    ): Response<List<ImdCurrentWxResponse>>

    /**
     * Automatic Weather Station (AWS) data by Station ID or State ID.
     */
    @GET("aws_data")
    suspend fun getAwsData(
        @Query("id") stationId: String? = null,
        @Query("sid") stateId: String? = null
    ): Response<List<ImdAwsDataResponse>>

    /**
     * City Forecast by Station ID or coordinates.
     */
    @GET("cityforecastloc")
    suspend fun getCityForecastLoc(
        @Query("id") stationId: String = "42182"
    ): Response<List<ImdCityForecastResponse>>

    /**
     * Astronomical Sun and Moon rise/set times by Latitude and Longitude.
     */
    @GET("sunmoon")
    suspend fun getSunMoon(
        @Query("lat") latitude: Double = 26.9124,
        @Query("lon") longitude: Double = 75.7873
    ): Response<ImdSunMoonResponse>
}
