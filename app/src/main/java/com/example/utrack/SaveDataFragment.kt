package com.example.utrack

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SaveDataFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.savesession)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        // user accept and continue session
                        // TODO guardar la data de la sesion en la base de datos
                        sendMessageSaveSession()
                    })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        sendMessagenoSaveSession()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun sendMessageSaveSession(){
        Toast.makeText(
            this.context,
            getString(R.string.sessionsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this.context, DataActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to result layout

    private fun sendMessagenoSaveSession(){
        Toast.makeText(
            this.context,
            getString(R.string.sessionnotsaved),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this.context, MainActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to main activity
}
