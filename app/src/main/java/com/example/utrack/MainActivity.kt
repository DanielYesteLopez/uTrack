package com.example.utrack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //hideNav()
        setContentView(R.layout.mainpage)
        buttonsMainActivityManagement()
        doubleBackToExitPressedOnce = false
    }

     private fun buttonsMainActivityManagement(){
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

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Toast.makeText(this, "Bye Bye!", Toast.LENGTH_SHORT).show()
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


    private fun onExitButtonPressed() {
        this.finish()
        exitProcess(0)
    }

    private fun onSettingsButtonPressed() {
        val intent = Intent(applicationContext,SettingsActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onDataButtonPressed(){
        val intent = Intent(applicationContext,DataActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onTrainingButtonPressed(){
        val intent = Intent(applicationContext,TrainingActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun hideNav() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }else
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }
}
