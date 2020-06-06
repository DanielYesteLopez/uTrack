package com.example.utrack.presenters

import android.app.Activity
import android.content.Context

class PresenterShowData private constructor (private val context: Context) {

    companion object : SingletonHolder<PresenterShowData, Context>(::PresenterShowData)

/*    fun deleteSession(index: Int) {
        PresenterMaster.getInstance(context).deleteSession(index)
    }*/

    fun deleteAll() {
        PresenterMaster.getInstance(context).deleteAllSessions()
    }

    fun exportSession(path: String) {
        PresenterMaster.getInstance(context).exportSession(path)
    }

/*    fun getSessionList(): ArrayList<Session>? {
        return PresenterMaster.getInstance(context).getSessionList()
    }

    fun getSession(index: Int): Session? {
        return PresenterMaster.getInstance(context).getSession(index)
    }*/

    fun visualizeSessionList(activity: Activity) {
        PresenterMaster.getInstance(context).visualizeSessionList(activity)
    }
}