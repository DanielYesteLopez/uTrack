package com.example.utrack.Views

import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.utrack.R

class ViewShowExerciseFragment : DialogFragment() {

    private var onposButtonClickLisnter: OnClickListener? = null
    private var onnegButtonClickLisnter: OnClickListener? = null
    private var onconButtonClickLisnter: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return getFragmentDialog()
    }

    private fun getFragmentDialog(): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            onposButtonClickLisnter = OnClickListener { _, _ ->
                // user accept and continue session (the user do tha last exercise )
                sendMessageContinueSession()
            }

            onnegButtonClickLisnter = OnClickListener { _, _ ->
                // user finish training
                val mySaveFragment =
                    ViewSaveDataFragment()
                mySaveFragment.show(activity?.supportFragmentManager!!, R.string.notefication.toString())
                sendMessageStopSession()
            }

            onconButtonClickLisnter = OnClickListener { _, _ ->
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
// wonderfull setters
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