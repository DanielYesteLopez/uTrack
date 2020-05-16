package com.example.utrack.presenters

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.IBinder
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.model.Exercise
import com.example.utrack.model.Facade
import com.example.utrack.model.Session
import com.example.utrack.model.Training
import com.example.utrack.model.services.LocationService
import com.example.utrack.model.services.SensorListenerAccelerometer
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class PresenterMaster private constructor (context: Context) {
    val facade = Facade(context)
    private var con = context
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private val TAG = "MainActivity"
    private var locationService: LocationService? = null
    private var sensorListenerAccelerometro : SensorListenerAccelerometer = SensorListenerAccelerometer(context)

    private var session : Session? = null

    private var training : Training? = null

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

    fun addSession(_session: Session) {
        _session.let { training?.let { it1 -> it.setTraining(it1) } }
        facade.addNewSession(_session)
    }

    fun onNegSaveDataButtonPressed() {
        // TODO nada de momento
    }

    fun visualizeSessionList(activity: Activity) {
        facade.visualizeSessionList(activity)
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
        locationService?.stopLogging()
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
        locationService?.pouseLogging()
        Toast.makeText(
            con,
            con.resources?.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getTrainingInfo(): ArrayList<ArrayList<Double>>? {
        val info = locationService?.getTrainingLocationInfo()
        info?.add(sensorListenerAccelerometro.getAcceleracionInfo())
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

    fun clearDataLocation() {
        locationService?.clearData()
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

//    fun updateAcceleracion(accelerateActual: Float) {
//
//    }

    fun createNewSession() : Session {
        this.session = Session()
        return this.session!!
    }

    fun createDummyTraining() {
        val values : ArrayList<ArrayList<Double>>? = getTrainingInfo()
        val time = values?.get(0)!![0]
        val distance = values[1][0]
        val speed = values[2]
        val acce = values[3]
        val exercise = Exercise(time,distance,distance,speed,acce)
        training = Training(exercise,false)
    }

    fun createTrainingWithRecommendedExercise() {
        val values : ArrayList<ArrayList<Double>>? = getTrainingInfo()
        val time = values?.get(0)!![0]
        val distance = values[1][0]
        val speed = values[2]
        val acce = values[3]
        val exercise = Exercise(time,distance,distance,speed,acce)
        training = Training(exercise,true)
    }

    fun getRecommendedExerciseDescription(): String {
        return training?.getRecomendedExerciseDescrpcion()!!
    }

    fun changeUserAccount(userName: String, password: String, realName: String, accountEmail: String) {
        facade.changeUserAccount(userName, password, realName, accountEmail)
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


    /* presenter bluetooth */
//  fun onConnectDevicesBluetoothButtonPressed() {
//    }
//
//    fun onStartTrainingBluetoothButtonPressed() {
//
//    }

}