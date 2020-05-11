package com.example.utrack.presenters

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.model.services.LocationService
import com.example.utrack.model.services.SensorListenerAccelerometer
import com.example.utrack.views.*
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class PresenterTraining(context: Context) {
    private val TAG = "MainActivity"
    private val con = context
    private var locationService: LocationService? = null
    private var sensorListenerAccelerometro : SensorListenerAccelerometer? = null
    init {
        sensorListenerAccelerometro = SensorListenerAccelerometer(context)
    }

    /* presenter View Training */
    fun onBackTrainingButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onStopTrainingButtonPressed(applicationContext: Context) {
        val myExerciseFragment = FragmentShowExercise()
        myExerciseFragment.activity?.supportFragmentManager?.let {
            myExerciseFragment.show(it, R.string.notefication.toString())
        }
    }

    fun onResumeTrainingButtonPressed(applicationContext: Context) {
        // TODO("Not yet implemented")
    }

    fun onStartTrainingButtonPressed(applicationContext: Context) {
        sensorListenerAccelerometro.let { it?.resumeReading() }
        locationService?.startLogging()
        Toast.makeText(applicationContext,
            applicationContext.resources.getString(R.string.trainingprogress),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onPauseTrainingButtonPressed(applicationContext: Context) {
        sensorListenerAccelerometro.let { it?.pauseReading() }
        locationService?.stopLogging()
        Toast.makeText(
            applicationContext,
            applicationContext.resources.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
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

    fun onServiceConnected(className: ComponentName, service: IBinder) {
        this.locationService = (service as LocationService.LocationServiceBinder).service
        this.locationService?.startUpdatingLocation()
    }

    fun onServiceDisconnected(className: ComponentName) {
        this.locationService?.stopUpdatingLocation()
        this.locationService = null
    }

    fun registerSensorListenerAccelerate(){
        sensorListenerAccelerometro.let { it?.registerListener() }
    }


    fun unRegisterSensorListenerAccelerate(){
        sensorListenerAccelerometro.let { it?.unregisterListener() }
    }

    fun getAcceleration() : Float {
        return sensorListenerAccelerometro.let { it?.getAccelerateActual()!! }
    }

    fun getSpeedTrapezi() : Float{
        return sensorListenerAccelerometro.let { it?.getVelocityActual()!! }
    }

    fun getPositionTrapeze() : Float{
        return sensorListenerAccelerometro.let { it?.getPositionActual()!! }
    }

    fun getSpeedGPS() : Float {
        var value = 0.0F
        try {
            value = locationService.let { it?.getLastLocationSpeed()!! }
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
    fun onCanShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        //val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            con,
            con.resources.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun onNegShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        // user finish training
        //val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            con,
            con.getString(R.string.trainingsad),
            Toast.LENGTH_SHORT
        ).show()
        val mySaveFragment =
            FragmentSaveData()
        mySaveFragment.show(fragmentActivity.supportFragmentManager, con.getString(R.string.notefication))
    }
    fun onPosShowExerciseButtonPressed(fragmentActivity:FragmentActivity) {
        //val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            con,
            con.getString(R.string.trainingawesome),
            Toast.LENGTH_SHORT
        ).show()
    }
    /* presenter save data */
    fun onPosSaveDataButtonPressed(fragmentActivity: FragmentActivity) {
        //val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            con,
            con.resources.getString(R.string.sessionsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(con, ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(con,intent,null)
    }
    fun onNegSaveDataButtonPressed(fragmentActivity : FragmentActivity) {
        //val appContext :Context = fragmentActivity.applicationContext
        Toast.makeText(
            con,
            con.resources.getString(R.string.sessionnotsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(con, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(con,intent,null)
    }
    /* presenter bluetooth */
    fun onConnectDevicesBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
        //val appContext :Context = fragmentActivity.applicationContext
        val intent = Intent(con, ViewBluetoothPairing().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(con,intent,null)
    }
    fun onStartTrainingBluetoothButtonPressed(fragmentActivity: FragmentActivity) {
        Toast.makeText(
            con,
            con.resources.getString(R.string.trainingstart),
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