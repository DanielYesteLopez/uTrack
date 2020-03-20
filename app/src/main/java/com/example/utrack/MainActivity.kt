package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            //systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        //}

        setContentView(R.layout.mainpage)

    }

    fun onSettingsButtonPressed(view : View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun onDataButtonPressed(view: View){
        val intent = Intent(this, DataActivity::class.java)
        startActivity(intent)
    }

    fun onTrainingButtonPressed(view: View){
        val intent = Intent(this, TrainingActivity::class.java)
        startActivity(intent)
    }
}
/*test*/