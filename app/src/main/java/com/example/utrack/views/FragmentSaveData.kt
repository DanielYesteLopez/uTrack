package com.example.utrack.views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass
import com.example.utrack.presenters.PresenterTraining

class FragmentSaveData : MainFragmentClass() {

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

            builder.setMessage(R.string.savesession)
                .setPositiveButton(R.string.yes,onPosButtonClickListener)
                .setNegativeButton(R.string.no,onNegButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun sendPosButtonPressed(fragmentActivity: FragmentActivity){
        // TODO("Guardar la data de la session en la base de datos")
        presenterTraining.onPosSaveDataButtonPressed(fragmentActivity)
    } // go to result layout

    override fun sendNegButtonPressed(fragmentActivity: FragmentActivity){
        // User cancelled the dialog
        // nothing is saved
        presenterTraining.onNegSaveDataButtonPressed(fragmentActivity)
    } // go to main activity
}
