//package com.example.weatherapp
//
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import com.example.weatherapp.utils.Constants
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//data class weatherhelper ( ){
//
//
//    private fun getLocationWeatherDetails(latitude: Double, longitude: Double) {
//        if (Constants.isNetworkAvailable(context = this)) {
//            val retrofit = Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            val serviceApi = retrofit.create(WeatherServiceApi::class.java)
//            val call = serviceApi.getWeatherDetails(
//                latitude,
//                longitude,
//                Constants.APP_ID,
//                Constants.METRIC_UNIT
//            )
//            call.enqueue(object : Callback<WeatherResponse> {
//                override fun onResponse(
//                    call: Call<WeatherResponse>,
//                    response: Response<WeatherResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val weather = response.body()
////                        Toast.makeText(
////                            this@MainActivity,
////                            "Lat: ${weather?.coord?.lat}\nLon: ${weather?.coord?.lon}",
////                            Toast.LENGTH_LONG
////                        ).show()
////                        Toast.makeText( this@MainActivity, weather.toString(), Toast.LENGTH_SHORT).show()
//
//
//                        for (i in weather?.weather?.indices!!) {
//                            findViewById<TextView>(R.id.timesunset).text = convertTime(weather.sys.sunset.toLong())
//                            findViewById<TextView>(R.id.timesunise).text = convertTime(weather.sys.sunrise.toLong())
//                            findViewById<TextView>(R.id.tvStatus).text = weather.weather[i].description
//                            findViewById<TextView>(R.id.tvDate).text = convertDate(weather.dt.toLong())
//                            val weatherIcon = findViewById<ImageView>(R.id.ivWeather)
//                            when (weather.weather[0].main) {
//                                "Clear" -> weatherIcon.setImageResource(R.drawable.clear)
//                                "Clouds" -> weatherIcon.setImageResource(R.drawable.cloudy)
//                                "Rain","Drizzle"-> weatherIcon.setImageResource(R.drawable.rain)
//                                "Thunderstorm" -> weatherIcon.setImageResource(R.drawable.thunder)
//                                "Snow" -> weatherIcon.setImageResource(R.drawable.snow)
//                                "Mist", "Fog", "Haze", "Smoke" ->
//                                    weatherIcon.setImageResource(R.drawable.fog)
//                                else -> weatherIcon.setImageResource(R.drawable.info)
//                            }
//                            findViewById<TextView>(R.id.tvCity).text = weather.name
//                            findViewById<TextView>(R.id.tvTemp).text = weather.main.temp.toString()
//                            findViewById<TextView>(R.id.timehumidity).text = weather.main.humidity.toString() + "%"
//                            findViewById<TextView>(R.id.timepressure).text = weather.main.pressure.toString()
//                            findViewById<TextView>(R.id.timewind).text = "${(weather.wind.speed * 3.6).toInt()} km/h"
//                        }
//
//
//                    } else {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Something went wrong",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//                override fun onFailure(
//                    call: Call<WeatherResponse>,
//                    t: Throwable?
//                ) {
//
//                }
//            })
//        }
//    }
//
//
//
//}