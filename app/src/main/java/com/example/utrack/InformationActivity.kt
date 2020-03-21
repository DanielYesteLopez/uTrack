package com.example.utrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class InformationActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        setContentView(R.layout.information)
        val backButton = findViewById<ImageButton>(R.id.backButtonDataPage)
        backButton.setOnClickListener {
            onBackInformationButtonPressed()

        }
    }

    fun onBackInformationButtonPressed(){
        startActivity(Intent(application,SettingsActivity().javaClass))
        this.finish()
    }
}