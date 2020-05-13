package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterShowData

class ViewData : SecondViewClass() {
    //var presenterShowData : PresenterShowData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setContentView(R.layout.showdata)
        PresenterShowData.getInstance(this)

        PresenterShowData.getInstance(this).visualizeSessionList(this)

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

    override fun onResume() {
        PresenterShowData.getInstance(this).visualizeSessionList(this)
        super.onResume()
    }
}
