package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterAccountSettings

class ViewAccountSettings : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.accountsetting)
        PresenterAccountSettings.getInstance(this)
        val backButton = findViewById<ImageButton>(R.id.backButtonAccountSettingsPage)
        backButton.setOnClickListener {
            onBackAccountButtonSettingsPressed()
        }
        val saveButtonUserSettings = findViewById<ImageButton>(R.id.saveButtonAccountSettings)
        saveButtonUserSettings.setOnClickListener{
            val userName = findViewById<EditText>(R.id.accountNameEdit).text.toString()
            val password = findViewById<EditText>(R.id.accountPassEdit).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.accountPassCheckEdit).text.toString()
            val realName = findViewById<EditText>(R.id.accountRnameEdit).text.toString()
            val accountEmail = findViewById<EditText>(R.id.accountemailEdit).text.toString()
            PresenterAccountSettings.getInstance(this).changeUserAccount(userName,password,confirmPassword,realName,accountEmail,applicationContext)
            Toast.makeText(applicationContext,"Saved!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun onBackAccountButtonSettingsPressed(){
        val intent = Intent(application, ViewSettings().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
