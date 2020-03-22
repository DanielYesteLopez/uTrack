package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainingpage)
        val backButton = findViewById<ImageButton>(R.id.backButtonTrainingPage)
        backButton.setOnClickListener {
            onBackTrainingButtonPressed()

        }
    }

    fun onBackTrainingButtonPressed(){
        startActivity(Intent(application,MainActivity().javaClass))
        this.finish()
    }
}
