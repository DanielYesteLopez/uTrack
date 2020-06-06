package com.example.utrack.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Session() {
    private var id : Int = 0
    private var data : String = " "
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

    @SuppressLint("SimpleDateFormat")
    private fun setData() {
        val format = SimpleDateFormat("yyyy_dd/MM_HH:mm")
        data = format.format(Date())
    }

/*
    fun setValues(_values : ArrayList<Double>){
        //this.values =  _values
        //val exercise : Exercise = Exercise(_values[0], )
        //training = Training(exercise)
    }

    fun getValues() : ArrayList<Double> {
        return this.values
    }*/

    override fun toString() : String {
        var s = ""
        s += "${getData()}: \n " + training.toString()
        return s
    }

    fun setTraining(_training: Training) {
        this.training = _training
    }
}