package com.example.utrack.model

class Exercise constructor(_duration: Double, _distance: Double, _accelerationAVG:Double, _speedList: ArrayList<Double>, _cadenceList: ArrayList<Double>) {

    private var duration: Double = _duration
    private var distance: Double = _distance
    private var speedList: ArrayList<Double> = _speedList
    private var cadenceList: ArrayList<Double> = _cadenceList
    private var acceleration: Double = _accelerationAVG

    fun getDuration() : Double {
        return duration
    }

    fun setDuration(newDuration: Double) {
        duration = newDuration
    }

    fun getDistance() : Double {
        return distance
    }

    fun setDistance(newDistance: Double) {
        distance = newDistance
    }

    fun getAcceleration() : Double {
        return acceleration;
    }

    fun setAcceleration(newAcceleration: Double) {
        acceleration = newAcceleration
    }

    fun getSpeedList() : ArrayList<Double> {
        return speedList
    }

    fun getCadenceList() : ArrayList<Double> {
        return cadenceList
    }

    fun getAverageSpeed(): Double{
        var temp: Double = 0.0
        for (value in speedList){
            temp += value
        }
        temp /= (speedList.size - 1)
        return temp
    }

    fun getAverageCadence(): Double{
        var temp: Double = 0.0
        for (value in cadenceList){
            temp += value
        }
        temp /= (cadenceList.size - 1)
        return temp
    }
    override fun toString(): String {
        var s: String = ""

        s += "Duration: ${duration} | Distance: ${distance} | Acceleration: ${acceleration} \n " +
                "S: ${speedList[1]} | C: ${cadenceList[1]}"
        return s
    }

}