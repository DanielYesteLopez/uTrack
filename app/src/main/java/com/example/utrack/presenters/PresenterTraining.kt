@file:Suppress("DEPRECATION")

package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.utrack.model.Session
import com.example.utrack.views.*
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.maps.model.LatLng

class PresenterTraining private constructor(private var context: Context) {

    //val EXTRA_MESSAGE_DEVICE: String = "extra device"
    private var isDoingRecommendedExercise = false
    private var hasBluetoothDevice = false
    private val _lTAG = "MainActivity"
    private var session : Session? = null
    companion object : SingletonHolder<PresenterTraining, Context>(::PresenterTraining)

    /* presenter View Training */
    fun onStopTrainingButtonPressed() {
        PresenterMaster.getInstance(context).onStopTrainingButtonPressed()
    }

    fun onResumeTrainingButtonPressed() {
        PresenterMaster.getInstance(context).onResumeTrainingButtonPressed()
    }

    fun onStartTrainingButtonPressed() {
       PresenterMaster.getInstance(context).onStartTrainingButtonPressed()
    }

    fun onPauseTrainingButtonPressed() {
        PresenterMaster.getInstance(context).onPauseTrainingButtonPressed()
    }

    fun onReceiveLocation(latLng: LatLng) {
        PresenterMaster.getInstance(context).onReceiveLocation(latLng)
    }

    fun onReceivePredictedLocation(latLng: LatLng) {
        PresenterMaster.getInstance(context).onReceivePredictedLocation(latLng)
    }

    fun onServiceConnected(service: IBinder) {
        PresenterMaster.getInstance(context).onServiceConnected(service)
    }

    fun onServiceDisconnected() {
        PresenterMaster.getInstance(context).onServiceDisconnected()
    }

    fun registerSensorListenerAccelerate(){
        PresenterMaster.getInstance(context).registerSensorListenerAccelerate()
    }

    fun unRegisterSensorListenerAccelerate(){
        PresenterMaster.getInstance(context).unRegisterSensorListenerAccelerate()
    }

    fun getAcceleration() : Double {
        return  PresenterMaster.getInstance(context).getAcceleration()
    }

/*    fun getTimeTraining() : Double {
        return  PresenterMaster.getInstance(context).getTimeInSeconds()
    }

    fun getTrainigInfo(): ArrayList<ArrayList<Double>>? {
        return PresenterMaster.getInstance(context).getTrainingInfo()
    }
    */

    fun getSpeedGPS() : Float {
        return  PresenterMaster.getInstance(context).getSpeedGPS()
    }

    fun getSpeedGPSAVG(): Float {
        return PresenterMaster.getInstance(context).getSpeedGPSAVG()
    }

    fun getDistanceGPS() : Float {
        return  PresenterMaster.getInstance(context).getDistanceGPS()
    }

    fun clearDataTraining() {
        PresenterMaster.getInstance(context).clearDataLocation()
    }

    fun isDoingRecommendedExercise() : Boolean {
        return this.isDoingRecommendedExercise
    }
    /* presenter show recommended exercise */
    fun onCanShowExerciseButtonPressed() {
        // do nothing
        Log.d(_lTAG, "user cancel && stop training")
        isDoingRecommendedExercise = false
    }

    fun onNegShowExerciseButtonPressed() {
        Log.d(_lTAG,"create dummy recommended exercise")
        PresenterMaster.getInstance(context).createDummyTraining()
        isDoingRecommendedExercise = false
    }

    fun onPosShowExerciseButtonPressed() {
        Log.d(_lTAG,"positive button")
        PresenterMaster.getInstance(context).createTrainingWithRecommendedExercise()
        isDoingRecommendedExercise = true
    }

    fun onPosSaveDataButtonPressed() {
        isDoingRecommendedExercise = false
        session?.let { PresenterMaster.getInstance(context).addSession(it) }
        val intent = Intent(context, ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ContextCompat.startActivity(context, intent,null)
    }

    fun onNegSaveDataButtonPressed() {
        isDoingRecommendedExercise = false
        PresenterMaster.getInstance(context).cancelFinishTraining()
        val intent = Intent(context, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ContextCompat.startActivity(context,intent,null)
    }

    /* presenter view bluetooth */
    fun onBluetoothDeviceChosen(_device: BleDevice) {
        hasBluetoothDevice = true
        PresenterMaster.getInstance(context).onBluetoothDeviceChosen(_device)
    }

    fun getDeviceCadence(): BleDevice? {
        if(hasBluetoothDevice){
            return PresenterMaster.getInstance(context).getCadenceSensor()
        } else {
            return null
        }
    }

    fun createNewSession() {
        session = PresenterMaster.getInstance(context).createNewSession()
    }

    fun getDescriptionRecommendedExercise(): String {
        return PresenterMaster.getInstance(context).getRecommendedExerciseDescription()
    }
}