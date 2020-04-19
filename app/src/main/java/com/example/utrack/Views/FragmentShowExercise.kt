package com.example.utrack.Views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass

class FragmentShowExercise : MainFragmentClass() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    override fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            onPosButtonClickListener = OnClickListener { _, _ ->
                // user accept and continue session (the user do tha last exercise )
                sendMessagePosButtonPressed()
            }

            onNegButtonClickListener = OnClickListener { _, _ ->
                // user finish training
                val mySaveFragment =
                    FragmentSaveData()
                mySaveFragment.show(activity?.supportFragmentManager!!, R.string.notefication.toString())
                sendMessageNegButtonPressed()
            }

            onCanButtonClickListener = OnClickListener { _, _ ->
                // User cancelled the dialog
                sendMessageCanButtonPressed()
            }
            //  TODO use class exercise recomend to show the recomended exercise
            builder.setMessage(R.string.recomendedexercise)
                .setPositiveButton(R.string.recomendedexerciseyes, onPosButtonClickListener)
                .setNegativeButton(R.string.recomendedexerciseno, onNegButtonClickListener)
                .setNeutralButton(R.string.continuesesion, onCanButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun sendMessagePosButtonPressed() {
        Toast.makeText(
            this.context,
            getString(R.string.trainingawesome),
            Toast.LENGTH_SHORT
        ).show()
    } // do recomended exercise

    override fun sendMessageNegButtonPressed() {
        Toast.makeText(
            this.context,
            getString(R.string.trainingawesome),
            Toast.LENGTH_SHORT
        ).show()
    } // continue Session

    override fun sendMessageCanButtonPressed() {
        Toast.makeText(
            this.context,
            getString(R.string.trainingsad),
            Toast.LENGTH_SHORT
        ).show()
    } // stop Session
}