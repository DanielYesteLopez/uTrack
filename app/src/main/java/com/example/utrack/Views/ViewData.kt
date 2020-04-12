package com.example.utrack.Views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewData : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setContentView(R.layout.showdata)
        val backButton = findViewById<ImageButton>(R.id.backButtonDataPage)
        backButton.setOnClickListener {
            onBackDataButtonPressed()

        }

    }

    private fun onBackDataButtonPressed(){
        val intent = Intent(application, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
