package com.example.utrack.model

import android.util.Log
import kotlin.math.roundToInt

class Exercise constructor(_duration: Double, _distance: Double, _accelerationAVG:Double, _speedList: ArrayList<Double>, _cadenceList: ArrayList<Double>) {

    private var duration: Double = _duration
    private var distance: Double = _distance
    private var speedList: ArrayList<Double> = _speedList
    private var cadenceList: ArrayList<Double> = _cadenceList
    private var acceleration: Double = _accelerationAVG

    fun getDuration() : Double {
        Log.d("exercise ","-------------------------------------------------   get duration $duration m")
        val r = (duration % 60)/100
        var result = duration / 60
        result = result.roundToInt() + r
        Log.d("exercise ","-------------------------------------------------   get duration $result h")
        return result
    }

/*    fun setDuration(newDuration: Double) {
        duration = newDuration
    }

    fun setDistance(newDistance: Double) {
        distance = newDistance
    }*/

    fun getDistance() : Double {
        return distance
    }

    private fun getAcceleration() : Double {
        return acceleration
    }

/*    fun setAcceleration(newAcceleration: Double) {
        acceleration = newAcceleration
    }

    fun getSpeedList() : ArrayList<Double> {
        return speedList
    }

    fun getCadenceList() : ArrayList<Double> {
        return cadenceList
    }*/

    fun getAverageSpeed(): Double{
        return speedList[1]
    }

    fun getAverageCadence(): Double{
        return cadenceList[1]
    }

    override fun toString(): String {
        var s = ""
        val formatTemplate = "%.2f%3s"
        s += "Duration: ${formatTemplate.format(getDuration() ,"m")} | Distance: ${formatTemplate.format(getDistance(),"k")} | Acceleration: ${formatTemplate.format(getAcceleration(),"m/s2")} \n " +
                "Speed: ${formatTemplate.format(getAverageSpeed(),"kph")} | Cadence: ${formatTemplate.format(getAverageSpeed(),"rpm")}"
        return s
    }
}