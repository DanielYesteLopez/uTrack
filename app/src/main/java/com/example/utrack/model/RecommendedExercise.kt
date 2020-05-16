package com.example.utrack.model

class RecommendedExercise constructor(_duration: Double, _description: String, _intensity: Int) {
    var duration: Double = _duration
    var description: String = _description
    var intensity: Int = _intensity

    fun get_Duration() : Double {
        return duration
    }

    fun set_Duration(newDuration: Double) {
        duration = newDuration
    }

    fun get_Description() : String {
        return description
    }

    fun set_Description(newDescription: String) {
        description = newDescription
    }

    fun get_Intensity() : Int {
        return intensity
    }

    fun set_Intesity(newIntesity: Int) {
        intensity = newIntesity
    }


    override fun toString(): String {
        var s: String = ""

        s += "Intensity: ${intensity} | Duration: ${duration}"
        return s
    }
}