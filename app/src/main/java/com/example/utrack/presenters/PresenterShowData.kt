package com.example.utrack.presenters

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.utrack.model.Session
import com.example.utrack.R

class PresenterShowData private constructor (context : Context) {
    //var presenterMaster : PresenterMaster? = PresenterMaster()
    private val con = context

    companion object : SingletonHolder<PresenterShowData, Context>(::PresenterShowData)

    fun addSession(session: Session) {
        PresenterMaster.getInstance(con).addSession(session)
    }

    fun deleteSession(index: Int) {
        PresenterMaster.getInstance(con).deleteSession(index)
    }

    fun deleteAll() {
        PresenterMaster.getInstance(con).deleteAll()
    }

    fun exportSession(path: String) {
        PresenterMaster.getInstance(con).exportSession(path)
    }

    fun getSessionList(): ArrayList<Session>? {
        return PresenterMaster.getInstance(con).getSessionList()
    }

    fun getSession(index: Int): Session? {
        return PresenterMaster.getInstance(con).getSession(index)
    }

    fun visualizeSessionList(activity: Activity) {
        PresenterMaster.getInstance(con).visualizeSessionList(activity)
    }
}