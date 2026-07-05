package com.example.weatherapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.models.CoordinatesResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewLocationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val location = intent.getStringExtra("Location")
        getcoordinate(location)

    }

    private fun getcoordinate(location: String?) {
        val coordRepo = CountryToCoord()
        if (location != null) {
            coordRepo.getCoordinates(location) { coordinates ->
                if (coordinates != null && coordinates.isNotEmpty()) {
                    val weatherRepo = WeatherRepo()
                    weatherRepo.getWeather(
                        coordinates[0].lat,
                        coordinates[0].lon
                    ) { weather ->
                        displayData(weather,coordinates[0])
                    }
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }



    //To display the data
    private fun displayData(weather: WeatherResponse?, coordinate: CoordinatesResponse) {
        findViewById<TextView>(R.id.tvCity).text = coordinate.name
        for (i in weather?.weather?.indices!!) {
            findViewById<TextView>(R.id.timesunset).text = convertTime(weather.sys.sunset.toLong())
            findViewById<TextView>(R.id.timesunise).text = convertTime(weather.sys.sunrise.toLong())
            findViewById<TextView>(R.id.tvStatus).text = weather.weather[i].description
            findViewById<TextView>(R.id.tvDate).text = convertDate(weather.dt.toLong())
            val weatherIcon = findViewById<ImageView>(R.id.ivWeather)
            when (weather.weather[0].main) {
                "Clear" -> weatherIcon.setImageResource(R.drawable.clear)
                "Clouds" -> weatherIcon.setImageResource(R.drawable.cloudy)
                "Rain", "Drizzle" -> weatherIcon.setImageResource(R.drawable.rain)
                "Thunderstorm" -> weatherIcon.setImageResource(R.drawable.thunder)
                "Snow" -> weatherIcon.setImageResource(R.drawable.snow)
                "Mist", "Fog", "Haze", "Smoke" ->
                    weatherIcon.setImageResource(R.drawable.fog)

                else -> weatherIcon.setImageResource(R.drawable.info)
            }

            findViewById<TextView>(R.id.tvTemp).text = weather.main.temp.toString()
            findViewById<TextView>(R.id.timehumidity).text = weather.main.humidity.toString() + "%"
            findViewById<TextView>(R.id.timepressure).text = weather.main.pressure.toString()
            findViewById<TextView>(R.id.timewind).text =
                "${(weather.wind.speed * 3.6).toInt()} km/h"
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



