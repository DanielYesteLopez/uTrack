package com.example.utrack.model

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.model.services.LocationService
import com.google.android.gms.maps.model.LatLng
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Facade (context : Context){
    private val user = User()
    private val database = Database()
    private var sessionList : SessionList? = null
    private val con = context
    private lateinit var arrayAdapter: ArrayAdapter<String>

    init {
        /*user = User()
        database = Database()*/
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
        sessionList?.addSession(session)
    }

    fun addNewSession(values: ArrayList<Double>?) {
        val arra = values
        val session : Session = Session()
        session.setId(getSessionList()?.size!!)
        if (arra != null) {
            session.setValues(arra)
        }
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
        } else {
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
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