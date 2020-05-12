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
    private lateinit var  arrayAdapter: ArrayAdapter<String>
    private val con = context
    companion object : SingletonHolder<PresenterShowData, Context>(::PresenterShowData)

    fun addSession(session: Session) {
        PresenterMaster.getInstance(con).addSession(session)
    }

    fun deleteSession(index: Int) {
        PresenterMaster.getInstance(con).deleteSession(index)
    }

    fun deleteAll(){
        PresenterMaster.getInstance(con).deleteAll()
    }

    fun exportSession(path: String) {
        PresenterMaster.getInstance(con).exportSession(path)
    }

    fun getSessionList() : ArrayList<Session>? {
        return PresenterMaster.getInstance(con).getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return PresenterMaster.getInstance(con).getSession(index)
    }

     fun visualizeSessionList(activity : Activity) {
        val sessionsList = getSessionList()

        // Create an array adapter
        arrayAdapter =  ArrayAdapter<String>(con, android.R.layout.simple_list_item_1)

        if (sessionsList?.isNotEmpty()!!) {
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
            // Set item click listener
            activity.findViewById<ListView>(R.id.showDataList).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    var actual_session = getSession(position)
                    if (actual_session != null) {
                        Toast.makeText(con, actual_session.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                    // take user back to training page
                }
            }
        }
    }