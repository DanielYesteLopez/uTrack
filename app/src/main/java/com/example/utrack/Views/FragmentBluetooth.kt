package com.example.utrack.Views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass

class FragmentBluetooth : MainFragmentClass() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    override fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            setOnPositiveButtonClickListener(OnClickListener { _, _ ->
                sendMessagePosButtonPressed()

            })

            setOnNegativeButtonClickListener(OnClickListener { _, _ ->
                sendMessageNegButtonPressed()
            })

            builder.setMessage(R.string.bluetooth)
                .setPositiveButton(R.string.choose_device, onPosButtonClickListener)
                .setNegativeButton(R.string.continue_without_device, onNegButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    override fun sendMessagePosButtonPressed(){
        Toast.makeText(
            this.context,
            getString(R.string.sessionsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this.context, ViewBluetoothPairing().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to result layout

}