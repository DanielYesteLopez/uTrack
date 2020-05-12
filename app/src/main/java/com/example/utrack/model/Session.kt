package com.example.utrack.model

import java.text.SimpleDateFormat
import java.util.*

class Session {
   private var id : Int = 0
   private var data : String = " "

    init {
        setData()
    }

    fun getId() : Int {
        return id
    }

    fun setId(newId : Int) {
        id = newId
    }

    fun getData() : String {
        return data
    }

    private fun setData() {
        val format = SimpleDateFormat("yyyy_MMdd_HHmm")
        data = format.format(Date())
    }
}