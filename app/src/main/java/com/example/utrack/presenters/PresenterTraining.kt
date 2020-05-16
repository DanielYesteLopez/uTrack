package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.utrack.model.Session
import com.example.utrack.views.*
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

class PresenterTraining private constructor(context: Context) {

    //val EXTRA_MESSAGE_DEVICE: String = "extra device"
    private var isDoingRecomendedExercise = false
    private val TAG = "MainActivity"
    private var con : Context = context
    companion object : SingletonHolder<PresenterTraining, Context>(::PresenterTraining)

    private var session : Session? = null

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

    fun getTrainigInfo(): ArrayList<ArrayList<Double>>? {
        return PresenterMaster.getInstance(con).getTrainingInfo()
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

   /* fun getAcceleration() : Float {
        return  PresenterMaster.getInstance(con).getAcceleration()
    }

    fun getSpeedTrapezi() : Float{
        return PresenterMaster.getInstance(con).getSpeedTrapezi()
    }

    fun getPositionTrapeze() : Float {
        return  PresenterMaster.getInstance(con).getPositionTrapeze()
    }*/

    fun getSpeedGPS() : Float {
        return  PresenterMaster.getInstance(con).getSpeedGPS()
    }

    fun getSpeedGPSAVG(): Float {
        return PresenterMaster.getInstance(con).getSpeedGPSAVG()
    }

    fun getDistanceGPS() : Float {
        return  PresenterMaster.getInstance(con).getDistanceGPS()
    }

    fun clearDataTraining() {
        PresenterMaster.getInstance(con).clearDataLocation()
    }

    /* presenter show recommended exercise */
    fun onCanShowExerciseButtonPressed() {
        // do nothing
        Log.d(TAG, "user cancel stop training")
        isDoingRecomendedExercise = false
    }

    fun onNegShowExerciseButtonPressed() {
        Log.d(TAG,"create dummy recomended exercise")
        PresenterMaster.getInstance(con).createDummyTraining()
        isDoingRecomendedExercise = false
    }

    fun onPosShowExerciseButtonPressed() {
        Log.d(TAG,"positive button")
        PresenterMaster.getInstance(con).createTrainingWithRecommendedExercise()
        isDoingRecomendedExercise = true
    }
    fun isDoingRecommendedExercise() : Boolean {
        return this.isDoingRecomendedExercise
    }

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
        session?.let { PresenterMaster.getInstance(con).addSession(it) }
    }

    fun onNegSaveDataButtonPressed() {
        PresenterMaster.getInstance(con).onNegSaveDataButtonPressed()
    }

    /* presenter view bluetooth */
    fun onBackBluetoothButtonPressed() {
        val intent = Intent(con, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.putExtra(EXTRA_MESSAGE_DEVICE,"0")
        ContextCompat.startActivity(con,intent,null)
    }

    fun onBluetoothDeviceChosen(_device: BluetoothDevice) {
        PresenterMaster.getInstance(con).onBluetoothDeviceChosen(_device)
    }

    fun createNewSession() {
        session = PresenterMaster.getInstance(con).createNewSession()
    }

    fun getDescriptionRecommendedExercise(): String {
        return PresenterMaster.getInstance(con).getRecommendedExerciseDescription()
    }

    fun getDeviceCadence(): BluetoothDevice? {
        return PresenterMaster.getInstance(con).getCadenceSensor()
    }

    fun goToTrainingView() {
        val intent = Intent(con, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.putExtra(EXTRA_MESSAGE_DEVICE,"1")
        ContextCompat.startActivity(con,intent,null)
    }
}