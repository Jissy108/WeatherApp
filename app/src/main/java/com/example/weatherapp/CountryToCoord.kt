package com.example.weatherapp

import com.example.weatherapp.models.CoordinatesResponse
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryToCoord {
    fun getCoordinates(cityName: String,countryCode: String,onResult: (List<CoordinatesResponse>?) -> Unit){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceApi = retrofit.create(WeatherServiceApi::class.java)
        val call = serviceApi.getCoordinates(
            cityName,
            countryCode,
            Constants.limit,
            Constants.APP_ID
        )
        call.enqueue(object : Callback<List<CoordinatesResponse>> {
            override fun onResponse(
                call: Call<List<CoordinatesResponse>>,
                response: Response<List<CoordinatesResponse>>
            ) {
                if (response.isSuccessful) {
                    val coordinate = response.body()
                    onResult(coordinate)
                } else {
                    onResult(null)
                }
            }
            override fun onFailure(
                call: Call<List<CoordinatesResponse>>,
                t: Throwable?
            ) {
                onResult(null)
            }
        })
    }

}