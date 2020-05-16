package com.example.utrack.model

import android.content.Context
import kotlin.math.roundToInt

class Training constructor (exercise: Exercise, boolean: Boolean, con: Context) {
    private var recommendedExercise: RecommendedExercise? = null
    private var exercise: Exercise? = exercise
    private var con = con

    init {
        if(boolean){
            recommendExercise()
        } else {
           setDummyExercise()
        }
    }

    private fun setDummyExercise() {
        recommendedExercise = RecommendedExercise(1, con)
    }

    fun recommendExercise() {
        var cadense : Double = exercise?.getAverageCadence()!!
        var speed : Double = exercise?.getAverageSpeed()!!
        var distance : Double = exercise?.getDistance()!!
        var duration : Double = exercise?.getDuration()!!
        var intesity = 0.0

        if (distance == 0.0 || speed == 0.0 || duration == 0.0){
            intesity = 1.0
        }
        else {
            distance /= 10
            distance *= 0.5
            duration /= 1000
            duration *= 0.5
            speed /= (exercise?.getAcceleration()!!)
            speed /= 10
            speed *= 0.5
            while(cadense > 1){
                cadense /= 10
            }
            intesity += duration + distance + cadense + speed
        }
        if (intesity > 8){
            intesity = 8.0
        }
        else if (intesity < 1.0){
            intesity = 1.0
        }
        recommendedExercise = RecommendedExercise(intesity.roundToInt(),con)
    }

    fun getRecomendedExerciseDescrpcion() : String {
        return this.recommendedExercise?.get_Description()!!
    }

    override fun toString() : String {
        var s = ""

        s += exercise.toString() + "\n" + recommendedExercise.toString()
        return s
    }
}