package com.example.utrack

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ShowExerciseRecommended : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.recomendedexercise)
                .setPositiveButton(R.string.recomendedexerciseyes,
                    DialogInterface.OnClickListener { dialog, id ->
                        // user accept and continue session

                    })
                .setNegativeButton(R.string.recomendedexerciseno,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog

                    })
            // Create the AlertDialog object and return it
            builder.create() } ?: throw IllegalStateException("Activity cannot be null")
    }
}
