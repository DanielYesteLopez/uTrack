package com.example.utrack.presenters

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utrack.R
import com.example.utrack.model.Facade
import com.example.utrack.model.Session
import com.example.utrack.model.services.LocationService
import com.example.utrack.model.services.SensorListenerAccelerometer
import com.example.utrack.views.ViewMainPage
import com.example.utrack.views.ViewTraining
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class PresenterMaster private constructor (context: Context) {
    val facade = Facade()
    private var con = context
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private val TAG = "MainActivity"
    private var locationService: LocationService? = null
    private var sensorListenerAccelerometro : SensorListenerAccelerometer = SensorListenerAccelerometer(context)

    companion object : SingletonHolder<PresenterMaster, Context>(::PresenterMaster)

    fun addNewUser(userDataMap: MutableMap<String, String>) {
        facade.setUserData(userDataMap)
    }

    fun setBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {
        facade.setBikeSettings(userBikeSettingsMap)
    }

    fun initializeBikeDatabase(userId: String) {
        facade.initializeBikeDatabase(userId)
    }

    fun addSession(session: Session) {
        facade.addSession(session)

    }

    fun deleteSession(index: Int) {
        facade.deleteSession(index)
    }

    fun deleteAll(){
        facade.deleteAll()
    }

    fun exportSession(path: String) {
        facade.exportSession(path)
    }

    fun getSessionList() : ArrayList<Session>? {
        return facade.getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return facade.getSession(index)
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
    fun onPosSaveDataButtonPressed() {
        val arra = getTrainigInfo()
        val session : Session = Session()
        session.setId(getSessionList()?.size!!)
        if (arra != null) {
            session.setValues(arra)
        }
        addSession(session)
    }

    fun onNegSaveDataButtonPressed() {
        // TODO nada de momento
    }

    fun visualizeSessionList(activity: Activity) {
        val sessionsList = getSessionList()
        // Create an array adapter
        arrayAdapter =  ArrayAdapter<String>(con, android.R.layout.simple_list_item_1)

        if (sessionsList?.isNotEmpty()!!) {
            for (session : Session in sessionsList) {
                arrayAdapter.add(session.toString())
            }
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
            // Set item click listener
            activity.findViewById<ListView>(R.id.showDataList).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val actual_session = getSession(position)
                    if (actual_session != null) {
                        Toast.makeText(con, actual_session.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    /* presenter bluetooth */
//  fun onConnectDevicesBluetoothButtonPressed() {
//    }
//
//    fun onStartTrainingBluetoothButtonPressed() {
//
//    }

}