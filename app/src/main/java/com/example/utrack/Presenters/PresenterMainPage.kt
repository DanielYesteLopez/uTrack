package com.example.utrack.Presenters

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.utrack.Views.ViewData
import com.example.utrack.Views.ViewMainPage
import com.example.utrack.Views.ViewSettings
import com.example.utrack.Views.ViewTraining
import kotlin.system.exitProcess

class PresenterMainPage {
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