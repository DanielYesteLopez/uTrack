package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class UserSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun onBackUserSettingsButtonPressed(){
        startActivity(Intent(application,SettingsActivity().javaClass))
        this.finish()
    }
}
