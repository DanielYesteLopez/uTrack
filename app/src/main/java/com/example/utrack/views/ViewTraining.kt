package com.example.utrack.views

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import kotlinx.android.synthetic.main.trainingpage.*

class ViewTraining : SecondViewClass() {

    private var presenterTraining = PresenterTraining()
    private var myBluetoothFragment = FragmentBluetooth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        // start activity
        setContentView(R.layout.trainingpage)

        // check bluetooth connection

        myBluetoothFragment.show(supportFragmentManager, getString(R.string.notefication))

        c_meter.base = SystemClock.elapsedRealtime()

        var isWorking = false
        var ispaused = true
        var pauseOffset: Long = 0

        // set layout visibility
        buttonStart.visibility = View.VISIBLE
        buttonStop.visibility = View.INVISIBLE
        buttonPause.visibility = View.INVISIBLE
        buttonResume.visibility = View.INVISIBLE
        textViewstartresume.visibility = View.VISIBLE
        textViewpause.visibility = View.INVISIBLE
        textViewstop.visibility = View.INVISIBLE

        // button llisteners
        buttonStart.setOnClickListener {
            if (!isWorking) {
                buttonStart.visibility = View.INVISIBLE
                buttonStop.visibility = View.VISIBLE
                buttonPause.visibility = View.VISIBLE
                buttonResume.visibility = View.INVISIBLE
                textViewstartresume.visibility = View.INVISIBLE
                textViewpause.visibility = View.VISIBLE
                textViewstop.visibility = View.VISIBLE
                textViewpause.setText(R.string.trainingpouse)
                // start timer (crono metro)
                c_meter.base = SystemClock.elapsedRealtime() - pauseOffset
                c_meter.start()
                isWorking = true
                ispaused = false
                presenterTraining.onStartTrainingButtonPressed(this@ViewTraining)
            }
        }

        buttonResume.setOnClickListener {
            if (!isWorking) {
                buttonStart.callOnClick()
                presenterTraining.onResumeTrainingButtonPressed(this@ViewTraining)
            }
        }

        buttonPause.setOnClickListener {
            if (isWorking) {
                buttonStart.visibility = View.INVISIBLE
                buttonStop.visibility = View.VISIBLE
                buttonPause.visibility = View.INVISIBLE
                buttonResume.visibility = View.VISIBLE
                textViewstartresume.visibility = View.INVISIBLE
                textViewpause.visibility = View.VISIBLE
                textViewstop.visibility = View.VISIBLE
                textViewpause.setText(R.string.trainingresume)
                c_meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - c_meter.base
                isWorking = false
                ispaused = true
                presenterTraining.onPauseTrainingButtonPressed(this@ViewTraining)
            }
        }

        buttonStop.setOnClickListener {
            if(isWorking || ispaused) {
                buttonPause.callOnClick()
                presenterTraining.onStopTrainingButtonPressed(this@ViewTraining)
            }
        }
        backButtonTrainingPage.setOnClickListener {
            presenterTraining.onBackTrainingButtonPressed(this@ViewTraining)
        }
        // exit on create
    }


}
