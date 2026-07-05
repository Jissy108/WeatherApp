package com.example.weatherapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ActivityNextLocationBinding
import com.example.weatherapp.models.CoordinatesResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewLocationScreen : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        val location = intent.getStringExtra("Location")
        getcoordinate(location)

    }

    private fun getcoordinate(location: String?) {
        val coordRepo = CountryToCoord()
        if (location != null) {
            coordRepo.getCoordinates(location) { coordinates ->
                if (coordinates != null && coordinates.isNotEmpty()) {
                    val coordinate = coordinates.firstOrNull()
                    val weatherRepo = WeatherRepo()
                    if (coordinate != null) {
                        weatherRepo.getWeather(
                            coordinate.lat,
                            coordinate.lon
                        ) { weather ->
                            WeatherDisplay.displayData(mBinding, weather, coordinate.name)
                        }
                    }
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}


