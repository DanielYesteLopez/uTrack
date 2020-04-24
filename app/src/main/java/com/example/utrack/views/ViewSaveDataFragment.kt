package com.example.utrack.views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.utrack.R

class ViewSaveDataFragment : DialogFragment() {

    private var onPosSaveButtonClickLisnter: OnClickListener? = null
    private var onNegSaveButtonClickLisnter: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    private fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            onPosSaveButtonClickLisnter = OnClickListener { _, _ ->
                // user accept and continue session
                // TODO guardar la data de la sesion en la base de datos
                sendMessageSaveSession()
            }

            onNegSaveButtonClickLisnter = OnClickListener { _, _ ->
                // User cancelled the dialog
                sendMessagenoSaveSession()
            }

            builder.setMessage(R.string.savesession)
                .setPositiveButton(R.string.yes,onPosSaveButtonClickLisnter)
                .setNegativeButton(R.string.no,onNegSaveButtonClickLisnter)
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
        val intent = Intent(this.context, ViewData().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    } // go to result layout

    private fun sendMessagenoSaveSession(){
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
