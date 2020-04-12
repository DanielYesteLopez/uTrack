package com.example.utrack.Views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.utrack.R

class ViewSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //hideNav()
        setContentView(R.layout.settingspage)
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