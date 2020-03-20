package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showdata)
    }

    fun onBackDataButtonPressed(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
