package com.example.utrack.ic

import android.app.Dialog
import android.content.DialogInterface

interface FragmentInterface {
    // var
    var onPosButtonClickListener: DialogInterface.OnClickListener?
    var onNegButtonClickListener: DialogInterface.OnClickListener?
    var onCanButtonClickListener: DialogInterface.OnClickListener?
    // main func
    fun getFragmentDialog(): Dialog
    // wonderful setters
    fun setOnPositiveButtonClickListener(onPosButtonClickListener: DialogInterface.OnClickListener?)
    fun setOnNegativeButtonClickListener(onNegButtonClickListener: DialogInterface.OnClickListener?)
    fun setOnNeutralButtonClickListener(onConButtonClickListener: DialogInterface.OnClickListener?)
    // wonderful getters
    fun getOnPositiveButtonClickListener() : DialogInterface.OnClickListener?
    fun getOnNegativeButtonClickListener() : DialogInterface.OnClickListener?
    fun getOnNeutralButtonClickListener() : DialogInterface.OnClickListener?
    // message
    fun sendMessagePosButtonPressed()
    fun sendMessageNegButtonPressed()
    fun sendMessageCanButtonPressed()
}