package com.example.utrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class InformationActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.information)
    }

    fun onBackInformationButtonPressed(view:View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}