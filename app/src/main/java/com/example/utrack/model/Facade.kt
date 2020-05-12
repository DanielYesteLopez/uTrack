package com.example.utrack.model

import android.provider.ContactsContract
import android.util.Log
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Facade {
    val user = User()
    val database = Database()
    var sessionList : SessionList? = null

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

    fun addSession(session: Session) {
        sessionList?.addSession(session)
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
}