package com.example.utrack.views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass
import com.example.utrack.presenters.PresenterTraining

class FragmentShowExercise : MainFragmentClass() {
    private var presenterTraining = PresenterTraining()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    override fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            setOnPositiveButtonClickListener(
                OnClickListener { _, _ -> sendPosButtonPressed(it) }
            )
            setOnNegativeButtonClickListener(
                OnClickListener { _, _ -> sendNegButtonPressed(it) }
            )
            setOnNeutralButtonClickListener(
                OnClickListener { _, _ -> sendCanButtonPressed(it) }
            )

            // TODO("use class exercise recommend to show the recommended exercise")
            builder.setMessage(R.string.recomendedexercise)
                .setPositiveButton(R.string.recomendedexerciseyes, onPosButtonClickListener)
                .setNegativeButton(R.string.recomendedexerciseno, onNegButtonClickListener)
                .setNeutralButton(R.string.continuesesion, onCanButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun sendPosButtonPressed(fragmentActivity: FragmentActivity) {
        // user accept and continue session (the user do tha last exercise )
        // user training progress should partitioned and the training should continue
        presenterTraining.onPosShowExerciseButtonPressed(fragmentActivity)
    } // do recomended exercise

    override fun sendCanButtonPressed(fragmentActivity: FragmentActivity) {
        // User cancelled the dialog
        presenterTraining.onCanShowExerciseButtonPressed(fragmentActivity)
    } // continue Session should resume the training

    override fun sendNegButtonPressed(fragmentActivity: FragmentActivity) {
        // user finish training
        presenterTraining.onNegShowExerciseButtonPressed(fragmentActivity)
    } // stop Session (user is been asked nether save the session or not)
}