package com.example.utrack.model

import android.annotation.SuppressLint
import android.util.Log
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SessionList {
    private var sessionList: ArrayList<Session>? = null

    init {
        sessionList = ArrayList()
    }

    fun addSession(session: Session) {
        sessionList?.add(session)
    }

    fun deleteSession(index: Int) {
        if (sessionList?.isEmpty()!!) {
            Log.d("deleteSession", "The sessionList is empty")
        } else if (index >= sessionList?.size!!) {
            Log.d("deleteSession", "The index is incorrect")
        } else {
            sessionList?.removeAt(index)
        }
    }

    fun deleteAll(){
        if (sessionList?.isEmpty()!!) Log.d("deleteAll", "The list is empty")
        else sessionList?.clear()
    }

    fun getSession(index: Int) : Session? {
        if (sessionList?.isEmpty()!!) {
            Log.d("getSession", "The list is empty")
            return null
        }
        else if (index >= sessionList!!.size){
            Log.d("getSession", "Index is incorrect")
            return null
        }
        return sessionList!![index]
    }

    fun getSessionList() : ArrayList<Session>? {
        return sessionList
    }

    @SuppressLint("SimpleDateFormat")
    fun exportSession(path: String) {
        val fileNameDateTimeFormat = SimpleDateFormat("yyyy_MMdd_HHmm")
        val filePath = (path + "/"
                + fileNameDateTimeFormat.format(Date()) + ".csv")

        Log.d("exportSession()", "saving to $filePath")

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(filePath, false)
            for (session: Session in sessionList!!) {
                fileWriter.append("${session.toString()} \n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close()
                } catch (ioe: IOException) {
                    ioe.printStackTrace()
                }
            }
        }
    }
}