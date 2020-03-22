package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import kotlinx.android.synthetic.*

class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showdata)
        val backButton = findViewById<ImageButton>(R.id.backButtonDataPage)
        backButton.setOnClickListener {
            onBackDataButtonPressed()

        }

    }


    private fun onBackDataButtonPressed(){
        startActivity(Intent(application,MainActivity().javaClass))
        this.finish()

    }
}
