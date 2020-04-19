package com.example.utrack.views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass

class FragmentSaveData : MainFragmentClass() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    override fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            onPosButtonClickListener = OnClickListener { _, _ ->
                // user accept and continue session
                // TODO guardar la data de la sesion en la base de datos
                sendMessagePosButtonPressed()
            }

            onNegButtonClickListener = OnClickListener { _, _ ->
                // User cancelled the dialog
                sendMessageNegButtonPressed()
            }

            builder.setMessage(R.string.savesession)
                .setPositiveButton(R.string.yes,onPosButtonClickListener)
                .setNegativeButton(R.string.no,onNegButtonClickListener)
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
        val intent = Intent(this.context, ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to result layout

    override fun sendMessageNegButtonPressed(){
        Toast.makeText(
            this.context,
            getString(R.string.sessionnotsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this.context, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to main activity
}
