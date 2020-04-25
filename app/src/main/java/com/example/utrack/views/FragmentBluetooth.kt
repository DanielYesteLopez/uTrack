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

class FragmentBluetooth : MainFragmentClass() {

    private val presenterTraining = PresenterTraining()

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
            builder.setMessage(R.string.bluetooth)
                .setPositiveButton(R.string.choose_device, onPosButtonClickListener)
                .setNegativeButton(R.string.continue_without_device, onNegButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    override fun sendPosButtonPressed(fragmentActivity:FragmentActivity){
        presenterTraining.onConnectDevicesBLuetoothButtonPressed(fragmentActivity)
    } // go to bluetooth fragment

    override fun sendNegButtonPressed(fragmentActivity: FragmentActivity) {
        presenterTraining.onStartTrainingBLuetoothButtonPressed(fragmentActivity)
    }
}