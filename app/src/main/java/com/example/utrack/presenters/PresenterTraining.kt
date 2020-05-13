package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.utrack.views.*
import com.google.android.gms.maps.model.LatLng

class PresenterTraining private constructor(context: Context) {
    private val TAG = "MainActivity"
    private var con : Context = context
    companion object : SingletonHolder<PresenterTraining, Context>(::PresenterTraining)


    /* presenter View Training */
    fun onBackTrainingButtonPressed() {
        val intent = Intent(con, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(con,intent,null)
    }

    fun onStopTrainingButtonPressed() {
        PresenterMaster.getInstance(con).onStopTrainingButtonPressed()
    }

    fun onResumeTrainingButtonPressed() {
        PresenterMaster.getInstance(con).onResumeTrainingButtonPressed()
    }

    fun onStartTrainingButtonPressed() {
       PresenterMaster.getInstance(con).onStartTrainingButtonPressed()
    }

    fun onPauseTrainingButtonPressed() {
        PresenterMaster.getInstance(con).onPauseTrainingButtonPressed()
    }

    fun getTrainigInfo(): ArrayList<Double>? {
        return PresenterMaster.getInstance(con).getTrainigInfo()
    }

    fun onReceiveLocation(latLng: LatLng) {
        PresenterMaster.getInstance(con).onReceiveLocation(latLng)
    }

    fun onReceivePredictedLocation(latLng: LatLng) {
        PresenterMaster.getInstance(con).onReceivePredictedLocation(latLng)
    }

    fun onServiceConnected(service: IBinder) {
        PresenterMaster.getInstance(con).onServiceConnected(service)
    }

    fun onServiceDisconnected() {
        PresenterMaster.getInstance(con).onServiceDisconnected()
    }

    fun registerSensorListenerAccelerate(){
        PresenterMaster.getInstance(con).registerSensorListenerAccelerate()
    }

    fun unRegisterSensorListenerAccelerate(){
        PresenterMaster.getInstance(con).unRegisterSensorListenerAccelerate()
    }

    fun getAcceleration() : Float {
        return  PresenterMaster.getInstance(con).getAcceleration()
    }

    fun getSpeedTrapezi() : Float{
        return PresenterMaster.getInstance(con).getSpeedTrapezi()
    }

    fun getPositionTrapeze() : Float {
        return  PresenterMaster.getInstance(con).getPositionTrapeze()
    }

    fun getSpeedGPS() : Float {
        return  PresenterMaster.getInstance(con).getSpeedGPS()
    }

    fun getDistanceGPS() : Float {
        return  PresenterMaster.getInstance(con).getDistanceGPS()
    }

    /* presenter show recommended exercise */
//    fun onCanShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
//        //val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            fragmentActivity.applicationContext,
//            fragmentActivity.resources.getString(R.string.trainingpaused),
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//
//    fun onNegShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
//        // user finish training
//        val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            appContext,
//            appContext.getString(R.string.trainingsad),
//            Toast.LENGTH_SHORT
//        ).show()
//        val mySaveFragment =
//            FragmentSaveData()
//        mySaveFragment.show(fragmentActivity.supportFragmentManager, R.string.notefication.toString())
//    }
//
//    fun onPosShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
//        val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            appContext,
//            appContext.getString(R.string.trainingawesome),
//            Toast.LENGTH_SHORT
//        ).show()
//    }
    /* presenter save data */
//    fun onPosSaveDataButtonPressed(fragmentActivity: FragmentActivity) {
//        val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            appContext,
//            appContext.resources.getString(R.string.sessionsaved),
//            Toast.LENGTH_SHORT
//        ).show()
//        val intent = Intent(appContext, ViewData().javaClass)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        ContextCompat.startActivity(appContext,intent,null)
//    }
//
//    fun onNegSaveDataButtonPressed(fragmentActivity : FragmentActivity) {
//        val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            appContext,
//            appContext.resources.getString(R.string.sessionnotsaved),
//            Toast.LENGTH_SHORT
//        ).show()
//        val intent = Intent(appContext, ViewMainPage().javaClass)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        ContextCompat.startActivity(appContext,intent,null)
//    }

    /* presenter bluetooth */
//    fun onConnectDevicesBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
//        val appContext :Context = fragmentActivity.applicationContext
//        val intent = Intent(appContext, ViewBluetoothPairing().javaClass)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        ContextCompat.startActivity(appContext,intent,null)
//    }
//    fun onStartTrainingBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
//        val appContext :Context = fragmentActivity.applicationContext
//        Toast.makeText(
//            appContext,
//            appContext.resources.getString(R.string.trainingstart),
//            Toast.LENGTH_SHORT
//        ).show()
//    }

    fun onPosSaveDataButtonPressed() {
        PresenterMaster.getInstance(con).addSession()
    }

    fun onNegSaveDataButtonPressed() {
        PresenterMaster.getInstance(con).onNegSaveDataButtonPressed()
    }

    /* presenter view bluetooth */
    fun onBackBluetoothButtonPressed() {
        val intent = Intent(con, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ContextCompat.startActivity(con,intent,null)
    }

    fun onBluetoothDeviceChosen(_device: BluetoothDevice) {
        PresenterMaster.getInstance(con).onBluetoothDeviceChosen(_device)
    }
}