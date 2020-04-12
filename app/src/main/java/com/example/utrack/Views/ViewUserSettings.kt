package com.example.utrack.Views

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewUserSettings : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //hideNav()
        setContentView(R.layout.usersettings)
        val backButton = findViewById<ImageButton>(R.id.backButtonUserSettingsPage)
        backButton.setOnClickListener {
            onBackUserSettingsButtonPressed()

        }
        val saveButtonUserSettings = findViewById<ImageButton>(R.id.saveButtonAccountSettings)
        saveButtonUserSettings.setOnClickListener{
            onSaveButtonUserSettingsPressed()
        }
    }

    private fun onSaveButtonUserSettingsPressed() {
        /*val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popupsave_usersettings,userSettingsLayout)
        val popupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        val buttonYes = view.findViewById<Button>(R.id.buttonYesPopUpSaveUserSettings)
        val buttonNo = view.findViewById<Button>(R.id.buttonNoPopUpSaveUserSettings)
        buttonNo.setOnClickListener{
            popupWindow.dismiss()
        }
        buttonYes.setOnClickListener{
            popupWindow.dismiss()
        }
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext,"Popup closed", Toast.LENGTH_SHORT).show()
        }

        popupWindow.showAtLocation(
            userSettingsLayout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )*/
        Toast.makeText(applicationContext,"Saved!",Toast.LENGTH_SHORT).show()
    }

    private fun onBackUserSettingsButtonPressed(){
        val intent = Intent(application, ViewSettings().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
