package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utrack.R
import com.example.utrack.model.services.LocationService
import com.example.utrack.model.services.SensorListenerAccelerometer
import com.example.utrack.views.*
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class PresenterTraining private constructor(context: Context) {

    private val TAG = "MainActivity"
    private var con : Context = context
    private var locationService: LocationService? = null
    private var sensorListenerAccelerometro : SensorListenerAccelerometer = SensorListenerAccelerometer(context)

    companion object : SingletonHolder<PresenterTraining, Context>(::PresenterTraining)


    /* presenter View Training */
    fun onBackTrainingButtonPressed() {
        val intent = Intent(con, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(con,intent,null)
    }
    fun onStopTrainingButtonPressed() {
        Toast.makeText(con,
            con.resources?.getString(R.string.trainingstop),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onResumeTrainingButtonPressed() {
        Toast.makeText(con,
            con.resources?.getString(R.string.trainingresume),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onStartTrainingButtonPressed() {
        sensorListenerAccelerometro.resumeReading()
        locationService?.startLogging()
        Toast.makeText(con,
            con.resources?.getString(R.string.trainingprogress),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onPauseTrainingButtonPressed() {
        sensorListenerAccelerometro.pauseReading()
        locationService?.stopLogging()
        Toast.makeText(
            con,
            con.resources?.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getTrainigInfo(): ArrayList<Double>? {
        val info = locationService?.getTrainingLocationInfo()
        info?.add(sensorListenerAccelerometro.getAccelerationAVG())
        return info
    }

    fun onReceiveLocation(latLng: LatLng) {
        this.locationService?.let{
            if (it.isLogging) {
                Log.d(TAG," new ->> $latLng")
                Log.d(TAG,"is Logging")
            }
        }
    }

    fun onReceivePredictedLocation(latLng: LatLng) {
        this.locationService?.let{
            if (it.isLogging) {
                Log.d(TAG,"predicted ->> $latLng")
                //findViewById<TextView>(R.id.location).text = latLng.toString()
                Log.d(TAG,"is Logging")
            }
        }
    }

    fun onServiceConnected(service: IBinder) {
        this.locationService = (service as LocationService.LocationServiceBinder).service
        this.locationService?.startUpdatingLocation()
    }

    fun onServiceDisconnected() {
        this.locationService?.stopUpdatingLocation()
        this.locationService = null
    }

    fun registerSensorListenerAccelerate(){
        sensorListenerAccelerometro.registerListener()
    }


    fun unRegisterSensorListenerAccelerate(){
        sensorListenerAccelerometro.unregisterListener()
    }

    fun getAcceleration() : Float {
        return sensorListenerAccelerometro.getAccelerateActual()
    }

    fun getSpeedTrapezi() : Float{
        return sensorListenerAccelerometro.getVelocityActual()
    }

    fun getPositionTrapeze() : Float{
        return sensorListenerAccelerometro.getPositionActual()
    }

    fun getSpeedGPS() : Float {
        var value = 0.0F
        try {
            value = locationService.let { it?.getLocationSpeed()!! }
        }catch (e : Exception){
            Log.d(TAG, "error getting speed GPS")
        }
        return value
    }

    fun getDistanceGPS() : Float {
        var value = 0.0F
        try {
            value = locationService.let { it?.getDistanceLocations()!! }
        }catch (e : Exception){
            Log.d(TAG, "error getting distance GPS")
        }
        return value
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

    /* presenter view bluetooth */
    fun onBackBluetoothButtonPressed() {
        val intent = Intent(con, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ContextCompat.startActivity(con,intent,null)
    }

    fun onBluetoothDeviceChosen(_device: BluetoothDevice) {
        val deviceName = _device.name
        //val deviceHardwareAddress = device.address // MAC address
        Toast.makeText(
            con,
            deviceName,
            Toast.LENGTH_SHORT
        ).show()
        // connect Device
        // TODO
        // take user back to training page
        // TODO
    }
}