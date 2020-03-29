package com.example.utrack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        hideNav()
        // start activity
        setContentView(R.layout.trainingpage)
        // non global atributs
        val startTextView: TextView = findViewById(R.id.textViewstartresume)
        val pauseTextView: TextView = findViewById(R.id.textViewpause)
        val stopTextView: TextView = findViewById(R.id.textViewstop)
        val stopButton: ImageButton = findViewById(R.id.buttonStop)
        val pauseButton: ImageButton = findViewById(R.id.buttonPause)
        val resumeButton: ImageButton = findViewById(R.id.buttonResume)
        val startButton: ImageButton = findViewById(R.id.buttonStart)
        val backButton: ImageButton = findViewById(R.id.backButtonTrainingPage)
        val meter: Chronometer = findViewById(R.id.c_meter)
        meter.base = SystemClock.elapsedRealtime()
        var isWorking = false
        var ispaused = true
        var pauseOffset: Long = 0
        // set layout visibility
        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.INVISIBLE
        pauseButton.visibility = View.INVISIBLE
        resumeButton.visibility = View.INVISIBLE
        startTextView.visibility = View.VISIBLE
        pauseTextView.visibility = View.INVISIBLE
        stopTextView.visibility = View.INVISIBLE
        // button llisteners
        startButton.setOnClickListener {
            if (!isWorking) {
                startButton.visibility = View.INVISIBLE
                stopButton.visibility = View.VISIBLE
                pauseButton.visibility = View.VISIBLE
                resumeButton.visibility = View.INVISIBLE
                startTextView.visibility = View.INVISIBLE
                pauseTextView.visibility = View.VISIBLE
                stopTextView.visibility = View.VISIBLE
                pauseTextView.setText(R.string.trainingpouse)
                // start timer (crono metro)
                meter.base = SystemClock.elapsedRealtime() - pauseOffset
                meter.start()
                isWorking = true
                ispaused = false
                Toast.makeText(this@TrainingActivity,
                    getString(R.string.trainingprogress),
                    Toast.LENGTH_SHORT).show()
            }
        }

        resumeButton.setOnClickListener {
            if (!isWorking) { startButton.callOnClick() }
        }

        pauseButton.setOnClickListener {
            if (isWorking) {
                startButton.visibility = View.INVISIBLE
                stopButton.visibility = View.VISIBLE
                pauseButton.visibility = View.INVISIBLE
                resumeButton.visibility = View.VISIBLE
                startTextView.visibility = View.INVISIBLE
                pauseTextView.visibility = View.VISIBLE
                stopTextView.visibility = View.VISIBLE
                pauseTextView.setText(R.string.trainingresume)
                meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - meter.base
                isWorking = false
                ispaused = true
                Toast.makeText(
                    this@TrainingActivity,
                    getString(R.string.trainingpaused),
                    Toast.LENGTH_SHORT).show()
            }
        }

        stopButton.setOnClickListener {
            if(isWorking || ispaused) {
                val myExerciseFragment = ShowExerciseFragment()
                pauseButton.callOnClick()
                // go to show exercice recomendation message
                //resumeButton.callOnClick()
                myExerciseFragment.show(supportFragmentManager, R.string.notefication.toString())
            }
        }
        backButton.setOnClickListener { onBackTrainingButtonPressed() }
        // exit on create
    }

    private fun onBackTrainingButtonPressed() {
        val intent = Intent(application, MainActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun hideNav() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }
}
