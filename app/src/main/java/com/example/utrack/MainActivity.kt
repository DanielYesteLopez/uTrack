package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        hideNav()
        setContentView(R.layout.mainpage)
        buttonsMainActivityManagement()

    }
     fun buttonsMainActivityManagement(){
        val settingsButton = findViewById<ImageButton>(R.id.settingsButtonMainpage)
        val dataButton = findViewById<ImageButton>(R.id.dataButtonMainpage)
        val trainingButton = findViewById<ImageButton>(R.id.trainingButtonMainpage)
        val exitButton = findViewById<ImageButton>(R.id.exitButtonMainpage)
        settingsButton.setOnClickListener {
            onSettingsButtonPressed()
        }
        dataButton.setOnClickListener {
            onDataButtonPressed()
        }
        trainingButton.setOnClickListener {
            onTrainingButtonPressed()
        }
        exitButton.setOnClickListener{
            onExitButtonPressed()
        }

    }

    private fun onExitButtonPressed() {
        this.finish()
        exitProcess(0)
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
    fun hideNav() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }

}
