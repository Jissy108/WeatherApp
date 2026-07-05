package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NextLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_next_location)

        val LocationCity = findViewById<EditText>(R.id.editText)
        val showBtn = findViewById<Button>(R.id.button)


        showBtn.setOnClickListener {
            val intent = Intent(this, NewLocationScreen::class.java)
            val Location= LocationCity.text.toString()
            intent.putExtra("Location", Location)
            startActivity(intent)
        }
        }

    }




