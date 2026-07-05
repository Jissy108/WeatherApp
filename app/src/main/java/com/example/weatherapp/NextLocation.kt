package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ActivityNextLocationBinding

class NextLocation : AppCompatActivity() {
    private lateinit var mBinding: ActivityNextLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_next_location
        )

//        val LocationCity = findViewById<EditText>(R.id.editText)
//        val showBtn = findViewById<Button>(R.id.button)
        mBinding.button.setOnClickListener {
            val intent = Intent(this, NewLocationScreen::class.java)
            val Location= mBinding.editText.text.toString()
            intent.putExtra("Location", Location)
            startActivity(intent)
        }



//        showBtn.setOnClickListener {
//            val intent = Intent(this, NewLocationScreen::class.java)
//            val Location= LocationCity.text.toString()
//            intent.putExtra("Location", Location)
//            startActivity(intent)
//        }
        }

    }




