package com.example.utrack.presenters

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.views.*

class PresenterTraining {
    /* presenter View Training */
    fun onBackTrainingButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }
    fun onStopTrainigButtonPressed(applicationContext: Context) {
        val myExerciseFragment =
            FragmentShowExercise()
        myExerciseFragment.activity?.supportFragmentManager?.let {
            myExerciseFragment.show(it, R.string.notefication.toString())
        }
    }
    fun onResumeTrainingButtonPressed(applicationContext: Context?) {
        // TODO("Not implemented yet")
    }
    fun onStartTrainigButtonPressed(applicationContext: Context?) {
        Toast.makeText(applicationContext,
            applicationContext?.resources?.getString(R.string.trainingprogress),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onPauseTrainigButtonPressed(applicationContext: Context?) {
        Toast.makeText(
            applicationContext,
            applicationContext?.resources?.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }
    /* presenter show recommended exercise */
    fun onCanShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        Toast.makeText(
            fragmentActivity.applicationContext,
            fragmentActivity.resources.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onNegShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        // user finish training
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.getString(R.string.trainingsad),
            Toast.LENGTH_SHORT
        ).show()
        val mySaveFragment =
            FragmentSaveData()
        mySaveFragment.show(fragmentActivity.supportFragmentManager, R.string.notefication.toString())
    }
    fun onPosShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.getString(R.string.trainingawesome),
            Toast.LENGTH_SHORT
        ).show()
    }
    /* presenter save data */
    fun onPosSaveDataButtonPressed(fragmentActivity: FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.resources.getString(R.string.sessionsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(appContext, ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(appContext,intent,null)
    }
    fun onNegSaveDataButtonPressed(fragmentActivity : FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.resources.getString(R.string.sessionnotsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(appContext, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(appContext,intent,null)
    }
    /* presenter bluetooth */
    fun onConnectDevicesBLuetoothButtonPressed(fragmentActivity: FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        val intent = Intent(appContext, ViewBluetoothPairing().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(appContext,intent,null)
    }
    fun onStartTrainingBLuetoothButtonPressed(fragmentActivity: FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.resources.getString(R.string.trainingstart),
            Toast.LENGTH_SHORT
        ).show()
    }
    /* presenter view bluetooth */
    fun onBackBluetoothButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onDiscoverBluetoothButtonPressed(applicationContext: Context){

    }
}