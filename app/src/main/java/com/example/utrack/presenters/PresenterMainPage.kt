package com.example.utrack.presenters

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.utrack.views.ViewData
import com.example.utrack.views.ViewMainPage
import com.example.utrack.views.ViewSettings
import com.example.utrack.views.ViewTraining
import kotlin.system.exitProcess

class PresenterMainPage {
    //Working presenter
    fun onSettingsButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSettings().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onDataButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onTrainingButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onExitButtonPressed(viewMainPage: ViewMainPage) {
        viewMainPage.finish()
        exitProcess(0)
    }


}