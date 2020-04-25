package com.example.utrack.mc

import android.content.DialogInterface
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.ic.FragmentInterface

abstract class MainFragmentClass : DialogFragment(), FragmentInterface {

    override var onPosButtonClickListener: DialogInterface.OnClickListener? = null
    override var onNegButtonClickListener: DialogInterface.OnClickListener? = null
    override var onCanButtonClickListener: DialogInterface.OnClickListener? = null

    // wonderful setters
    override fun setOnPositiveButtonClickListener(onPosButtonClickListener: DialogInterface.OnClickListener?) {
        this.onPosButtonClickListener = onPosButtonClickListener
    }

    override fun setOnNegativeButtonClickListener(onNegButtonClickListener: DialogInterface.OnClickListener?) {
        this.onNegButtonClickListener = onNegButtonClickListener
    }

    override fun setOnNeutralButtonClickListener(onConButtonClickListener: DialogInterface.OnClickListener?) {
        this.onCanButtonClickListener = onConButtonClickListener
    }

    // wonderful getters
    override fun getOnPositiveButtonClickListener() : DialogInterface.OnClickListener? {
        return this.onPosButtonClickListener
    }

    override fun getOnNegativeButtonClickListener() : DialogInterface.OnClickListener? {
        return this.onNegButtonClickListener
    }

    override fun getOnNeutralButtonClickListener() : DialogInterface.OnClickListener? {
        return this.onCanButtonClickListener
    }

    // message
    override fun sendPosButtonPressed(fragmentActivity: FragmentActivity){
        Toast.makeText(
            this.context, "Positive button pressed",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun sendNegButtonPressed(fragmentActivity: FragmentActivity){
        Toast.makeText(
            this.context, "Negative button pressed",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun sendCanButtonPressed(fragmentActivity: FragmentActivity){
        Toast.makeText(
            this.context, "Cancel button pressed",
            Toast.LENGTH_SHORT
        ).show()
    }
}