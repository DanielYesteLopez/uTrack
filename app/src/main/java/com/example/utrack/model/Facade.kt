package com.example.utrack.model

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.utrack.R
import kotlin.collections.ArrayList

class Facade (private val context: Context){
    private var cadenceDevice: Sensor? = null
    private val user = User()
    private val database = Database()
    private var sessionList : SessionList? = null
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
        sessionList?.addSession(session.toString())
        database.addNewSession(session.toString())
    }

    fun addNewSession(_session: Session) {
        val session : Session = _session
        session.setId(getSessionList()?.size!!)
        addSession(session)
    }

/*    fun deleteSession(index: Int) {
        sessionList?.deleteSession(index)
    }*/

    fun deleteAllSessions(){
        database.clearSessions()
        sessionList?.deleteAll()
    }

    fun exportSession(path: String) {
        sessionList?.exportSession(path)
    }

    private fun getSessionList() : ArrayList<String>? {
        return sessionList?.getSessionList()
    }

    private fun getSession(index : Int) : String? {
        return sessionList?.getSession(index)
    }

    fun visualizeSessionList(activity: Activity) {
        // Create an array adapter
        //val arrayCheck = database.getDatabaseSessions()
        val sessionsList = getSessionList()
        arrayAdapter =  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        if (sessionsList?.isNotEmpty()!!) {
            for (session : String in sessionsList) {
                if (session != "" && session != "0") {
                    arrayAdapter.insert(session,0)
                }
            }
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
            // Set item click listener
            activity.findViewById<ListView>(R.id.showDataList).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val actualSession = getSession(position)
                    if (actualSession != null) {
                        Toast.makeText(context, actualSession.subSequence(0,1),
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
        }
    }

/*    fun getSensorCadence(): BluetoothDevice? {
        return this.cadenceDevice?.getABluetoothDevice()
    }*/

    private fun getSavedSessions() : ArrayList<String> {
        return database.getDatabaseSessions()
    }

    private fun addSessionFireBase( s : String) {
        sessionList?.addSession(s)
    }

    fun addSessionsFromFireBase(){
        val sessions = getSavedSessions()
        for (session in sessions) {
            if (session != "0" && session != "") {
                addSessionFireBase(session)
            }
        }
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