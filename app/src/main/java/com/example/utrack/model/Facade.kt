package com.example.utrack.model

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.*
import com.example.utrack.R
import kotlin.collections.ArrayList

class Facade (private val context: Context){
    private var cadenceDevice: Sensor? = null
    private val user = User()
    private val database = Database()
    private var sessionList : SessionList? = null
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var anadido = false

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
        //sessionList?.addSession(session.toString())
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
/*        if(!anadido) {
            añadirSessionesdelFireBase()
            anadido = true
        }*/
        añadirSessionesdelFireBase()
        val sessionsList = getSessionList()
        arrayAdapter =  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        if (sessionsList?.isNotEmpty()!!) {
            for (session : String in sessionsList) {
                if (session != "" && session != "0") {
                    arrayAdapter.add(session)
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

    fun getSensorCadence(): BluetoothDevice? {
        return this.cadenceDevice?.getABluetoothDevice()
    }

    private fun getSavedSessions() {
        database.getDatabaseSessions()
        database.getDatabaseUserSettings()
    }

    private fun addSessionFireBase( session : String) {
        if (session != "" && session != "0")
        sessionList?.addSession(session)
    }

    fun recoverDataFromFireBase(){
        getSavedSessions()
    }

    fun añadirSessionesdelFireBase() {
        sessionList?.getSessionList()?.clear()
        addSessionFireBase(database.checkSession1)
        addSessionFireBase(database.checkSession2)
        addSessionFireBase(database.checkSession3)
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

    fun updateBikeSettings(
        findViewById: EditText,
        findViewById1: EditText,
        findViewById2: EditText,
        findViewById3: EditText,
        findViewById4: EditText
    ) {
        findViewById.hint = database.frameSize
        findViewById1.hint = database.height
        findViewById2.hint = database.diskTeeth
        findViewById3.hint = database.pinionTeeth
        findViewById4.hint = database.stem

    }
}