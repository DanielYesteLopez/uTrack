package com.example.utrack.model

class Training constructor (exercise: Exercise) {
    private var recommendedExercise: RecommendedExercise? = null
    private var exercise: Exercise? = exercise

    init {
        recommendExercise()
    }



    fun recommendExercise() {
        //TODO "dice un ejercicio recomendado"
        // algoritmo para recomendedExercise
        recommendedExercise = RecommendedExercise(10.0,"jaja",1)
    }

    override fun toString() : String {
        var s : String = ""

        s += exercise.toString() + "\n" + recommendedExercise.toString()
        return s
    }
}