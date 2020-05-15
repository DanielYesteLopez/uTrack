package com.example.utrack.model

class Training constructor (exercise: Exercise, boolean: Boolean) {
    private var recommendedExercise: RecommendedExercise? = null
    private var exercise: Exercise? = exercise

    init {
        if(boolean){
            recommendExercise()
        } else {
           setDummyExercise()
        }
    }

    private fun setDummyExercise() {
        recommendedExercise = RecommendedExercise(0.0,"dummy",0)
    }

    fun recommendExercise() {
        //TODO "dice un ejercicio recomendado"
        // algoritmo para recomendedExercise
        var cadense : Double = exercise?.getAverageCadence()!!
        var speed : Double = exercise?.getAverageSpeed()!!
        var acceleration : Double = exercise?.getAcceleration()!!
        var distance : Double = exercise?.getDistance()!!
        var duration : Double = exercise?.getDuration()!!
        var intesity : Int = 0;
        //cadencia =
        //speed =
        //accelerate =
        //distance =
        //duration = 2h

        //(duration)*acceleration;


        recommendedExercise = RecommendedExercise(10.0,"real recommended exercise",1)
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