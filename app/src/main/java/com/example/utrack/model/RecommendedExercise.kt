package com.example.utrack.model

import android.annotation.SuppressLint
import android.app.Application
import kotlinx.android.synthetic.main.signup.view.*
import android.content.res.Resources
import com.example.utrack.R

class RecommendedExercise
constructor( _intensity: Int) {
    var duration: Double = 0.0
    var description: String
    var exerciseList: ArrayList<String> = arrayListOf(
        Resources.getSystem().getString(R.string.recommended1),
        "Estirar las piernas por 5 minutos y descansar por otros 5 minutos",
        "Hacer vueltas a velocidad constante de 30 Km/h durante 10 min" ,
        "Hacer vueltas a velocidad constante de 30 Km/h durante 15 min",
        "Ejercicio progresivo durante 10 min empezando con velocidad minima 25 Km/h y luego pararse para estirar los musculos durante 5 min",
        "Vueltas a 40 Km/h durante 10 o 15 min, estirar las piernas y hacer ejercicios fisicos en el gimnasio durante 5 min",
        "Vueltas a velocidad constante de 40 Km/h por encima de la linea azul",
        "Vueltas a velocidad constante de 35 kph por encima de la linea azul, cada 5 vueltas cambiar a la linea negra para aumentar la velocidad a 45 kph durante 2 vueltas y volver a la azul a la velocidad anterior para 1 vuelta mas")
    lateinit var durationList: ArrayList<Double>
    var intensity: Int = _intensity

    init {
        this.description=exerciseList[intensity]
        this.duration=durationList[intensity]
    }
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
