package com.example.utrack

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ShowExerciseFragment : DialogFragment() {

    private var onposButtonClickLisnter: DialogInterface.OnClickListener? = null
    private var onnegButtonClickLisnter: DialogInterface.OnClickListener? = null
    private var onconButtonClickLisnter: DialogInterface.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return getFragmentDialog()
    }

    fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            onposButtonClickLisnter = DialogInterface.OnClickListener { dialog, id ->
                // user accept and continue session (the user do tha last exercise )
                sendMessageContinueSession()
            }

            onnegButtonClickLisnter = DialogInterface.OnClickListener { dialog, id ->
                // user finish training
                val mySaveFragment = SaveDataFragment()
                mySaveFragment.show(activity?.supportFragmentManager!!, R.string.notefication.toString())
                sendMessageStopSession()
            }

            onconButtonClickLisnter = DialogInterface.OnClickListener { dialog, id ->
                // User cancelled the dialog
                sendMessageContinueSession()
            }

            builder.setMessage(R.string.recomendedexercise)
                .setPositiveButton(R.string.recomendedexerciseyes, onposButtonClickLisnter)
                .setNegativeButton(R.string.recomendedexerciseno, onnegButtonClickLisnter)
                .setNeutralButton(R.string.continuesesion, onconButtonClickLisnter)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
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
    } // stop Session
/*
    fun setOnPositiveButtonClickLisnter(onPosButtonClickLisnter: DialogInterface.OnClickListener?) {
        this.onposButtonClickLisnter = onPosButtonClickLisnter
    }

    fun setOnNegativeButtonClickLisnter(onNegButtonClickLisnter: DialogInterface.OnClickListener?) {
        this.onnegButtonClickLisnter = onNegButtonClickLisnter
    }

    fun setOnNeutralButtonClickLisnter(onConButtonClickLisnter: DialogInterface.OnClickListener?) {
        this.onconButtonClickLisnter = onConButtonClickLisnter
    }*/
}