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

class PresenterMaster private constructor (private var context: Context) {
    private val facade = Facade(context)
    private val _lTAG = "PresenterMaster"
    private var locationService: LocationService? = null
    private var sensorListenerAccelerometer : SensorListenerAccelerometer = SensorListenerAccelerometer(context)

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

    fun cancelFinishTraining() {
        //do nothing
    }

    fun visualizeSessionList(activity: Activity) {
        facade.visualizeSessionList(activity)
    }

/*    fun deleteSession(index: Int) {
        facade.deleteSession(index)
    }*/

    fun deleteAll(){
        facade.deleteAll()
    }

    fun exportSession(path: String) {
        facade.exportSession(path)
    }

/*    fun getSessionList() : ArrayList<Session>? {
        return facade.getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return facade.getSession(index)
    }*/

    fun onStopTrainingButtonPressed() {
        locationService?.stopLogging()
        Toast.makeText(context,
            context.resources?.getString(R.string.trainingstop),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onResumeTrainingButtonPressed() {
        Toast.makeText(context,
            context.resources?.getString(R.string.trainingresume),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onStartTrainingButtonPressed() {
        sensorListenerAccelerometer.resumeReading()
        locationService?.startLogging()
        Toast.makeText(context,
            context.resources?.getString(R.string.trainingprogress),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onPauseTrainingButtonPressed() {
        sensorListenerAccelerometer.pauseReading()
        locationService?.pouseLogging()
        Toast.makeText(
            context,
            context.resources?.getString(R.string.trainingpaused),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getTrainingInfo() : ArrayList<ArrayList<Double>>? {
        val info = locationService?.getTrainingLocationInfo()
        info?.add(sensorListenerAccelerometer.getAccelerationInfo())
        return info
    }

    fun onReceiveLocation(latLng: LatLng) {
        this.locationService?.let{
            if (it.isLogging) {
                Log.d(_lTAG," new ->> $latLng")
                Log.d(_lTAG,"is Logging")
            }
        }
    }

    fun onReceivePredictedLocation(latLng: LatLng) {
        this.locationService?.let{
            if (it.isLogging) {
                Log.d(_lTAG,"predicted ->> $latLng")
                Log.d(_lTAG,"is Logging")
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
        sensorListenerAccelerometer.registerListener()
    }

    fun unRegisterSensorListenerAccelerate(){
        sensorListenerAccelerometer.unregisterListener()
    }

    fun getAcceleration() : Double {
        return sensorListenerAccelerometer.getAcceleration()
    }

    fun getTimeInSeconds() : Double {
        return locationService?.getTimeInSeconds()!!
    }


    /*
    fun getSpeedTrapezi() : Float{
        //return sensorListenerAccelerometro.getVelocityActual()
    }

    fun getPositionTrapeze() : Float{
        //return sensorListenerAccelerometro.getPositionActual()
    }
*/
    fun getSpeedGPS() : Float {
        var value = 0.0F
        try {
            value = locationService.let { it?.getLocationSpeed()!! }
        }catch (e : Exception){
            Log.d(_lTAG, "error getting speed GPS")
        }
        return value
    }


    fun getSpeedGPSAVG() : Float {
        var value = 0.0f
        try {
            value = locationService.let { it?.getLocationSpeedAVG()!! }
        }catch (e : Exception){
            Log.d(_lTAG, "error getting speed GPS")
        }
        return value
    }

    fun getDistanceGPS() : Float {
        var value = 0.0F
        try {
            value = locationService.let { it?.getDistanceLocations()!! }
        }catch (e : Exception){
            Log.d(_lTAG, "error getting distance GPS")
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
            context,
            deviceName,
            Toast.LENGTH_SHORT
        ).show()
        facade.setCadenceDevice(_device)
    }

/*    fun getCadenceSensor() : BluetoothDevice? {
        return facade.getSensorCadence()
    }*/

//    fun updateAcceleration(accelerateActual: Float) {
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
        val acct = values[3]
        val exercise = Exercise(time,distance,acct[1],speed,speed)
        training = Training(exercise,false, context)
    }

    fun createTrainingWithRecommendedExercise() {
        val values : ArrayList<ArrayList<Double>>? = getTrainingInfo()
        val time = values?.get(0)!![0]
        val distance = values[1][0]
        val speed = values[2]
        val acct = values[3]
        val exercise = Exercise(time,distance,acct[1],speed,speed)
        training = Training(exercise,true, context)
    }

    fun getRecommendedExerciseDescription(): String {
        return training?.getRecommendedExerciseDescription()!!
    }

    fun changeUserAccount(userName: String, password: String, realName: String, accountEmail: String) {
        facade.changeUserAccount(userName,password,realName,accountEmail)
    }

    fun initalizeSessionDatabase(userId: String) {
        facade.initializeSessionDatabase(userId)

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