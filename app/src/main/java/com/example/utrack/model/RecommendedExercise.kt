package com.example.utrack.model

import android.content.Context

import com.example.utrack.R

class RecommendedExercise
constructor( _intensity: Int, con: Context) {

    var duration: Double = 0.0
    private var description: String
    private var exerciseList = arrayListOf(
        con.getString(R.string.recommended1),
        con.getString(R.string.recommended2),
        con.getString(R.string.recommended3),
        con.getString(R.string.recommended4),
        con.getString(R.string.recommended5),
        con.getString(R.string.recommended6),
        con.getString(R.string.recommended7),
        con.getString(R.string.recommended8)
        )
    private var durationList = arrayListOf(
        5.0,
        5.0,
        10.0,
        15.0,
        15.0,
        15.0,
        20.0,
        20.0
    )
    private var intensity: Int = _intensity

    init {
        this.description=exerciseList[intensity - 1]
        this.duration=durationList[intensity - 1]
    }
/*    fun get_Duration() : Double {
        return duration
    }

    fun set_Duration(newDuration: Double) {
        duration = newDuration
    }*/

    fun getDescription() : String {
        return description
    }

/*
    fun set_Description(newDescription: String) {
        description = newDescription
    }

    fun get_Intensity() : Int {
        return intensity
    }

    fun set_Intesity(newIntesity: Int) {
        intensity = newIntesity
    }
*/


    override fun toString(): String {
        var s = ""

        s += "Intensity: $intensity | Duration: $duration"
        return s
    }
}