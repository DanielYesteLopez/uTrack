package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainpage)
        buttonsMainActivityManagement()

    }
     fun buttonsMainActivityManagement(){
        val settingsButton = findViewById<ImageButton>(R.id.settingsButtonMainpage)
        val dataButton = findViewById<ImageButton>(R.id.dataButtonMainpage)
        val trainingButton = findViewById<ImageButton>(R.id.trainingButtonMainpage)
        settingsButton.setOnClickListener {
            onSettingsButtonPressed()
        }
        dataButton.setOnClickListener {
            onDataButtonPressed()
        }
        trainingButton.setOnClickListener {
            onTrainingButtonPressed()
        }

    }


    fun onSettingsButtonPressed() {
        startActivity(Intent(applicationContext,SettingsActivity().javaClass))
        this.finish()
    }

    fun onDataButtonPressed(){
        startActivity(Intent(applicationContext,DataActivity().javaClass))
        this.finish()
    }

    fun onTrainingButtonPressed(){
        startActivity(Intent(applicationContext,TrainingActivity().javaClass))
        this.finish()
    }
}
/*test*/