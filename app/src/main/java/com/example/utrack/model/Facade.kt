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
        sessionList?.addSession(session)
    }

    fun addNewSession(_session: Session) {
        val session : Session = _session
        session.setId(getSessionList()?.size!!)
        addSession(session)
    }

/*    fun deleteSession(index: Int) {
        sessionList?.deleteSession(index)
    }*/

    fun deleteAll(){
        sessionList?.deleteAll()
    }

    fun exportSession(path: String) {
        sessionList?.exportSession(path)
    }

    private fun getSessionList() : ArrayList<Session>? {
        return sessionList?.getSessionList()
    }

    private fun getSession(index : Int) : Session? {
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
        arrayAdapter =  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)

        if (sessionsList?.isNotEmpty()!!) {
            for (session : Session in sessionsList) {
                arrayAdapter.insert(session.toString(),0)
            }
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
            // Set item click listener
            activity.findViewById<ListView>(R.id.showDataList).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val actualSession = getSession(position)
                    if (actualSession != null) {
                        Toast.makeText(context, actualSession.toString(),
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

    fun setCadenceDevice(_device: BluetoothDevice) {
        cadenceDevice?.setABluetoothDevice(_device)
    }

    /* presenter bluetooth */
//  fun onConnectDevicesBluetoothButtonPressed() {
//    }
//
//    fun onStartTrainingBluetoothButtonPressed() {
//
//    }
}