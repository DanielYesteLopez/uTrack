package com.example.utrack.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Session {
    private var id : Int = 0
    private var data : String = " "
    private var  values : ArrayList<Double> = ArrayList()
    private var training : Training? = null

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

    // Cambiar a training ( 2 listas, 1 con velocidad y otra con cadencia )
    // pasar el tiempo y aceleración del acelerómetro
    fun setValues(_values : ArrayList<Double>){
        this.values =  _values
    }

    fun getValues() : ArrayList<Double> {
        return this.values
    }

    // DANI JORGE: CAMBIAR ESTO POR EL toString del training
    override fun toString() : String {
        var s = ""
        s += "${getId()+1}: ${getData()} \n " +
                "${getValues()[0].toString().format(2)} " +
                "${getValues()[1].toString().format(2)} " +
                getValues()[2].toString().format(2)
        return s
    }

}