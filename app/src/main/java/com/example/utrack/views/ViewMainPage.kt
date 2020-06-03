package com.example.utrack.views


import android.os.Bundle
import android.widget.ImageButton
import com.example.utrack.presenters.PresenterMainPage
import com.example.utrack.R
import com.example.utrack.mc.MainViewClass

class ViewMainPage : MainViewClass() {
    //private var presenterMain  = PresenterMainPage()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setContentView(R.layout.mainpage)
        PresenterMainPage.getInstance(this)
        buttonsMainActivityManagement()
        doubleBackToExitPressedOnce = false
        PresenterMainPage.getInstance(this).recoverDataSessions()
    }

    fun buttonsMainActivityManagement(){
        val settingsButton = findViewById<ImageButton>(R.id.settingsButtonMainpage)
        val dataButton = findViewById<ImageButton>(R.id.dataButtonMainpage)
        val trainingButton = findViewById<ImageButton>(R.id.trainingButtonMainpage)
        val exitButton = findViewById<ImageButton>(R.id.exitButtonMainpage)
        settingsButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onSettingsButtonPressed(this.applicationContext)
        }
        dataButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onDataButtonPressed(this.applicationContext)
        }
        trainingButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onTrainingButtonPressed(this.applicationContext)
        }
        exitButton.setOnClickListener{
            PresenterMainPage.getInstance(this).onExitButtonPressed(this)
        }

    }
}
