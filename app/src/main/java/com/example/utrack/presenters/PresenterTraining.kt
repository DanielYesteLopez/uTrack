package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
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
    fun onStopTrainingButtonPressed(applicationContext: Context) {
        val myExerciseFragment =
            FragmentShowExercise()
        myExerciseFragment.activity?.supportFragmentManager?.let {
            myExerciseFragment.show(it, R.string.notefication.toString())
        }
    }
    fun onResumeTrainingButtonPressed(applicationContext: Context) {
        // TODO("Not yet implemented")
    }
    fun onStartTrainingButtonPressed(applicationContext: Context) {
        Toast.makeText(applicationContext,
            applicationContext.resources.getString(R.string.trainingprogress),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onPauseTrainingButtonPressed(applicationContext: Context) {
        Toast.makeText(
            applicationContext,
            applicationContext.resources.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }
    /* presenter show recommended exercise */
    fun onCanShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            appContext,
            appContext.resources.getString(R.string.trainingpaused),
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
    fun onConnectDevicesBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
        val appContext :Context = fragmentActivity.applicationContext
        val intent = Intent(appContext, ViewBluetoothPairing().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(appContext,intent,null)
    }
    fun onStartTrainingBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
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

    fun onBluetoothDeviceChosen(applicationContext: Context,_device: BluetoothDevice) {
        val deviceName = _device.name
        //val deviceHardwareAddress = device.address // MAC address
        Toast.makeText(
            applicationContext,
            deviceName,
            Toast.LENGTH_SHORT
        ).show()
        // connect Device
        // TODO
        // take user back to training page
        // TODO
    }
}