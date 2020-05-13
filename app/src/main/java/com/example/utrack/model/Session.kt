package com.example.utrack.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Session {
    private var id : Int = 0
    private var data : String = " "
    private var  values : ArrayList<Double> = ArrayList()

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

    fun setValues(_values : ArrayList<Double>){
        this.values =  _values
    }

    fun getValues() : ArrayList<Double> {
        return this.values
    }

    override fun toString() : String {
        var s = ""
        s += "${getId()+1}: ${getData()} \n " +
                "${getValues()[0].toString().format(2)} " +
                "${getValues()[1].toString().format(2)} " +
                getValues()[2].toString().format(2)
        return s
    }

}