package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewInformation : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.information)
        val backButton = findViewById<ImageButton>(R.id.backButtonInformationPage)
        backButton.setOnClickListener {
            onBackInformationButtonPressed()

        }
    }

    private fun onBackInformationButtonPressed(){
        val intent = Intent(application, ViewSettings().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}