package com.example.weatherapp

import com.example.weatherapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object WeatherDisplay {
    fun displayData(mBinding: ActivityMainBinding, weather: WeatherResponse?, cityName:String) {
        for (i in weather?.weather?.indices!!) {
//            findViewById<TextView>(R.id.timesunset).text = convertTime(weather.sys.sunset.toLong())
            mBinding.timesunset.text = convertTime(weather.sys.sunset.toLong())
//            findViewById<TextView>(R.id.timesunise).text = convertTime(weather.sys.sunrise.toLong())
            mBinding.timesunise.text = convertTime(weather.sys.sunrise.toLong())
//            findViewById<TextView>(R.id.tvStatus).text = weather.weather[i].description
            mBinding.tvStatus.text = weather.weather[i].description
//            findViewById<TextView>(R.id.tvDate).text = convertDate(weather.dt.toLong())
            mBinding.tvDate.text = convertDate(weather.dt.toLong())
//            val weatherIcon = findViewById<ImageView>(R.id.ivWeather)

            when (weather.weather[0].main) {
                "Clear" -> mBinding.ivWeather.setImageResource(R.drawable.clear)
                "Clouds" -> mBinding.ivWeather.setImageResource(R.drawable.cloudy)
                "Rain", "Drizzle" -> mBinding.ivWeather.setImageResource(R.drawable.rain)
                "Thunderstorm" -> mBinding.ivWeather.setImageResource(R.drawable.thunder)
                "Snow" -> mBinding.ivWeather.setImageResource(R.drawable.snow)
                "Mist", "Fog", "Haze", "Smoke" ->
                    mBinding.ivWeather.setImageResource(R.drawable.fog)
                else -> mBinding.ivWeather.setImageResource(R.drawable.info)
            }
//            findViewById<TextView>(R.id.tvCity).text = weather.name
            mBinding.tvCity.text = cityName
//            findViewById<TextView>(R.id.tvTemp).text = weather.main.temp.toString()
            mBinding.tvTemp.text = weather.main.temp.toString()
//            findViewById<TextView>(R.id.timehumidity).text = weather.main.humidity.toString() + "%"
            mBinding.timehumidity.text = weather.main.humidity.toString() + "%"
//            findViewById<TextView>(R.id.timepressure).text = weather.main.pressure.toString()
            mBinding.timepressure.text = weather.main.pressure.toString()
//            findViewById<TextView>(R.id.timewind).text =
//                "${(weather.wind.speed * 3.6).toInt()} km/h"
            mBinding.timewind.text = "${(weather.wind.speed * 3.6).toInt()} km/h"
        }
    }


    // Convert date and Time in standard form

    private fun convertTime(time: Long): String {
        val date = Date(time * 1000L)
        val timeFormatted = SimpleDateFormat("HH:mm", Locale.UK)
        timeFormatted.timeZone = TimeZone.getDefault()
        return timeFormatted.format(date)
    }

    private fun convertDate(time: Long): String {
        val date = Date(time * 1000L)

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        formatter.timeZone = TimeZone.getDefault()

        return formatter.format(date)
    }


}