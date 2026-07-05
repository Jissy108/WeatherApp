package com.example.weatherapp


import android.content.Context
import com.example.weatherapp.models.CoordinatesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("data/2.5/weather")
    fun getWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String,
        @Query("units") metric: String
    ): Call<WeatherResponse>


    @GET("geo/1.0/direct")
    fun getCoordinates(
        @Query("q") cityName: String,
        @Query("q") countryCode: String,
        @Query("limit") limit: Int,
        @Query("appid") appId: String
    ): Call<List<CoordinatesResponse>>

}