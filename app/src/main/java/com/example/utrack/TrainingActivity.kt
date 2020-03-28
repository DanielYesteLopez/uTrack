package com.example.utrack

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class TrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        hideNav()
        setContentView(R.layout.trainingpage)
        //
        val startResumeTextView :TextView = findViewById<TextView>(R.id.textViewstartresume)
        val pauseTextView :TextView = findViewById<TextView>(R.id.textViewpause)
        val stopTextView :TextView = findViewById<TextView>(R.id.textViewstop)
        val stopButton : ImageButton = findViewById<ImageButton>(R.id.buttonStop)
        val pauseButton : ImageButton = findViewById<ImageButton>(R.id.buttonPause)
        val resumeButton : ImageButton = findViewById<ImageButton>(R.id.buttonResume)
        val startButton : ImageButton = findViewById<ImageButton>(R.id.buttonStart)
        val backButton : ImageButton = findViewById<ImageButton>(R.id.backButtonTrainingPage)
        val meter = findViewById<Chronometer>(R.id.c_meter)
        //
        meter?.format = "Time: %s"
        meter?.base = SystemClock.elapsedRealtime()
        var isWorking = false
        var pauseOffset: Long = 0
        //
        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        startResumeTextView.visibility = View.VISIBLE
        pauseTextView.visibility = View.GONE
        stopTextView.visibility = View.GONE

        //
        startButton.setOnClickListener {
            //
            startButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            pauseButton.visibility = View.VISIBLE
            resumeButton.visibility = View.GONE
            //
            startResumeTextView.visibility = View.GONE
            pauseTextView.visibility = View.VISIBLE
            stopTextView.visibility = View.VISIBLE
            // set text to resume
            startResumeTextView.setText(R.string.trainingresume)
            // start timer (crono metro)
            if (!isWorking) {
                meter.base = SystemClock.elapsedRealtime() - pauseOffset
                meter.start()
                isWorking = true
            }
            Toast.makeText(this@TrainingActivity,
                getString(R.string.trainingprogress),
                Toast.LENGTH_SHORT).show()
        }

        resumeButton.setOnClickListener {
            //
            startButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            pauseButton.visibility = View.VISIBLE
            resumeButton.visibility = View.GONE
            //
            startResumeTextView.visibility = View.GONE
            pauseTextView.visibility = View.VISIBLE
            stopTextView.visibility = View.VISIBLE
            // start timer (crono metro)
            if (!isWorking) {
                meter.base = SystemClock.elapsedRealtime() - pauseOffset
                meter.start()
                isWorking = true
            }
            Toast.makeText(this@TrainingActivity,
                getString(R.string.trainingprogress),
                Toast.LENGTH_SHORT).show()
        }

        pauseButton.setOnClickListener {
            startButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
            resumeButton.visibility = View.VISIBLE
            //
            startResumeTextView.visibility = View.VISIBLE
            pauseTextView.visibility = View.GONE
            stopTextView.visibility = View.VISIBLE

            if (isWorking) {
                meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - meter.base
                isWorking = false
            }
            Toast.makeText(this@TrainingActivity,
                getString(R.string.trainingpaused),
                Toast.LENGTH_SHORT).show()
        }

        stopButton.setOnClickListener {

            startButton.visibility = View.VISIBLE
            stopButton.visibility = View.GONE
            pauseButton.visibility = View.GONE
            resumeButton.visibility = View.GONE
            //
            startResumeTextView.visibility = View.VISIBLE
            pauseTextView.visibility = View.GONE
            stopTextView.visibility = View.GONE

            startResumeTextView.setText(R.string.trainingstart)
            meter.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
            //stop
            meter.stop()
            pauseOffset = SystemClock.elapsedRealtime() - meter.base
            isWorking = false

            Toast.makeText(this@TrainingActivity,
                getString(R.string.trainingfinilized),
                Toast.LENGTH_SHORT).show()
            // go to show exercice recomendation page
            // TODO: not implemented yet
            // exit listener
        }

        backButton.setOnClickListener { onBackTrainingButtonPressed() }
    }

    private fun onBackTrainingButtonPressed(){
        val intent = Intent(application,MainActivity().javaClass)
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
        }else
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
