package com.example.utrack

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
                        sendMessageContinueSession() // continue Session
                    })
                .setNegativeButton(R.string.recomendedexerciseno,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        sendMessageStopSession() // go to result layout
                    })
            // Create the AlertDialog object and return it
            builder.create() } ?: throw IllegalStateException("Activity cannot be null")
    }
    private fun sendMessageContinueSession(){
        Toast.makeText(
            this.context,
            getString(R.string.trainingawesome),
            Toast.LENGTH_SHORT
        ).show()
    } // continue Session

    private fun sendMessageStopSession(){
        Toast.makeText(
            this.context,
            getString(R.string.trainingsad),
            Toast.LENGTH_SHORT
        ).show()
        // TODO go to result
        val intent = Intent(this.context, DataActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to result layout
}