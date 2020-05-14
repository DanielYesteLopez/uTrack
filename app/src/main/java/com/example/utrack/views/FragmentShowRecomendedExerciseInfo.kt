package com.example.utrack.views

import android.app.Dialog
import android.os.Bundle
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.utrack.R
import com.example.utrack.mc.MainFragmentClass
import com.example.utrack.presenters.PresenterTraining

class FragmentShowRecomendedExerciseInfo : MainFragmentClass() {

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
            val descripcionExercise = PresenterTraining.getInstance(this.requireContext()).getDescriptionRecommendedExercise()
            builder.setMessage(descripcionExercise)
                .setPositiveButton(R.string.ok,onPosButtonClickListener)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun sendPosButtonPressed(fragmentActivity: FragmentActivity){
        // do nothing
    }
}
