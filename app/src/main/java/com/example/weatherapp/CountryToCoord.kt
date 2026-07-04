package com.example.weatherapp

import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryToCoord {
    fun getCoordinates(cityName: String,limit: Int,appId: String,onResult: (WeatherResponse?) -> Unit){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceApi = retrofit.create(WeatherServiceApi::class.java)
        val call = serviceApi.getCountry(
            cityName,
            limit,
            appId
        )
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    onResult(weather)
                } else {
                    onResult(null)
                }
            }
            override fun onFailure(
                call: Call<WeatherResponse>,
                t: Throwable?
            ) {

            }
        })
    }

}