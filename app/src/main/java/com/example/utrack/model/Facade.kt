package com.example.utrack.model

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.utrack.R
import java.lang.Thread.sleep
import kotlin.collections.ArrayList

class Facade (context : Context){
    private var cadenceDevice: Sensor? = null
    private val user = User()
    private val database = Database()
    private var sessionList : SessionList? = null
    private val con = context
    private lateinit var arrayAdapter: ArrayAdapter<String>

    init {
        /*user = User()
        database = Database()*/
        cadenceDevice = Sensor()
        sessionList = SessionList()
    }
    fun setUserData(userDataMap: MutableMap<String, String>) {
        user.setUserData(userDataMap)
    }

    fun setBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {
        database.saveDatabaseBikeSettings(userBikeSettingsMap)
        user.setBikeSettings(userBikeSettingsMap)
    }

    fun initializeBikeDatabase(userId: String) {
        database.initializeBikeDatabase(userId)
    }

    private fun addSession(session: Session) {
        //TODO Guardar sesion en el database
        sessionList?.addSession(session)
        //Guardar sesion to String
        //Duracion
        //ID
        //....
        database.addNewSession(session.toString())
    }

    fun addNewSession(_session: Session) {
        val session : Session = _session
        session.setId(getSessionList()?.size!!)
        addSession(session)
    }

    fun deleteSession(index: Int) {
        sessionList?.deleteSession(index)
    }

    fun deleteAll(){
        sessionList?.deleteAll()
    }

    fun exportSession(path: String) {
        sessionList?.exportSession(path)
    }

    fun getSessionList() : ArrayList<Session>? {
        return sessionList?.getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return sessionList?.getSession(index)
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

    fun visualizeSessionList(activity: Activity) {
        val sessionsList = getSessionList()
        // Create an array adapter
        val arrayCheck = database.getDatabaseSessions()
        arrayAdapter =  ArrayAdapter<String>(con, android.R.layout.simple_list_item_1)
        if (arrayCheck[0].isNotEmpty()) {
            for (session in arrayCheck) {
                arrayAdapter.insert(session,0)
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
        } else {
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
        }
    }

    fun getSensorCadence(): BluetoothDevice? {
        return this.cadenceDevice?.getABluetoothDevice()
    }

    fun setCadenceDevice(_device: BluetoothDevice) {
        cadenceDevice?.setABluetoothDevice(_device)
    }

    fun changeUserAccount(userName: String, password: String, realName: String, accountEmail: String) {
        database.changeUserAccount(userName,password,realName,accountEmail)
        user.changeUserAccount(userName,realName,accountEmail)
    }

    fun initializeSessionDatabase(userId: String) {
        database.initializeSessionDatabase(userId)
    }

    /* presenter bluetooth */
//  fun onConnectDevicesBluetoothButtonPressed() {
//    }
//
//    fun onStartTrainingBluetoothButtonPressed() {
//
//    }
}