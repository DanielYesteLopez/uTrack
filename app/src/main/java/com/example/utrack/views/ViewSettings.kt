package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterSettings

class ViewSettings : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settingspage)
        PresenterSettings.getInstance(this)
        manageButtonsSettings()
    }

    private fun manageButtonsSettings() {
        val userSettinggsButton = findViewById<ImageButton>(R.id.imageButtonUserSettings_SettingsPage)
        val accountSettingsButton = findViewById<ImageButton>(R.id.accountButtonSettingsPage)
        val backSettingsButton = findViewById<ImageButton>(R.id.backButtonSettingsPage)
        val informationSettingsButton = findViewById<ImageButton>(R.id.informationButtonSettingsPage)
        userSettinggsButton.setOnClickListener{
            onUserSettingsButtonPressed()
        }
        accountSettingsButton.setOnClickListener{
            onAccountButtonPressed()
        }
        backSettingsButton.setOnClickListener{
            onBackSettingsButtonPressed()
        }
        informationSettingsButton.setOnClickListener{
            onInformationButtonPressed()
        }

    }

    private fun onUserSettingsButtonPressed(){
        val intent = Intent(this, ViewUserSettings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onAccountButtonPressed(){
        val intent = Intent(this, ViewAccountSettings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onBackSettingsButtonPressed(){
        val intent = Intent(this, ViewMainPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onInformationButtonPressed() {
        val intent = Intent(this, ViewInformation::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}