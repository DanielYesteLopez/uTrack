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

class PresenterShowData (context : Context) {
    var presenterMaster : PresenterMaster? = null
    private lateinit var  arrayAdapter: ArrayAdapter<String>
    private val _context = context

    init {
        presenterMaster = PresenterMaster()
    }

    fun addSession(session: Session) {
        presenterMaster?.addSession(session)
    }

    fun deleteSession(index: Int) {
        presenterMaster?.deleteSession(index)
    }

    fun deleteAll(){
        presenterMaster?.deleteAll()
    }

    fun exportSession(path: String) {
        presenterMaster?.exportSession(path)
    }

    fun getSessionList() : ArrayList<Session>? {
        return presenterMaster?.getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return presenterMaster?.getSession(index)
    }


     fun visualizeSessionList(activity : Activity) {
        val sessionsList = getSessionList()

        // Create an array adapter
        arrayAdapter =  ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1)

        if (sessionsList?.isNotEmpty()!!) {
            activity.findViewById<ListView>(R.id.showDataList).adapter = arrayAdapter
            // Set item click listener
            activity.findViewById<ListView>(R.id.showDataList).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    var actual_session = getSession(position)
                    if (actual_session != null) {
                        Toast.makeText(_context, actual_session.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                    // take user back to training page
                }
            }
        }
    }