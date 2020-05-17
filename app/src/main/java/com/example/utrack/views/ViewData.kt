package com.example.utrack.views

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterShowData
import kotlinx.android.synthetic.main.showdata.*
import java.text.SimpleDateFormat
import java.util.*

class ViewData : SecondViewClass() {
    //var presenterShowData : PresenterShowData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setContentView(R.layout.showdata)
        PresenterShowData.getInstance(this)
        PresenterShowData.getInstance(this).visualizeSessionList(this)

        val backButton = findViewById<ImageButton>(R.id.backButtonDataPage)
        backButton.setOnClickListener {
            onBackDataButtonPressed()
        }
        val clearButton = findViewById<ImageButton>(R.id.clearButtonDataPage)
        clearButton.setOnClickListener {
            onClearButtonPressed()
        }
        val exportButton = findViewById<ImageButton>(R.id.exportButtonDataPage)
        exportButton.setOnClickListener {
            onExportButtonPressed()
        }
    }

    private fun onBackDataButtonPressed(){
        val intent = Intent(application, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onResume() {
        PresenterShowData.getInstance(this).visualizeSessionList(this)
        super.onResume()
    }

    private fun onClearButtonPressed() {
        PresenterShowData.getInstance(this).deleteAll()
        Toast.makeText(this, "All sessions deleted", Toast.LENGTH_SHORT).show()
        PresenterShowData.getInstance(this).visualizeSessionList(this)
    }

    private fun onExportButtonPressed() {
        val filePath = (this.getExternalFilesDir(null)!!.absolutePath)

        PresenterShowData.getInstance(this).exportSession(filePath)
    }
}
