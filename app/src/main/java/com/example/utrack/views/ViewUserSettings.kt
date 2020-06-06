package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterSettings

class ViewUserSettings : SecondViewClass() {
    //var presenterSettings = PresenterSettings()
    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.usersettings)
        PresenterSettings.getInstance(this)
        val backButton = findViewById<ImageButton>(R.id.backButtonUserSettingsPage)
        backButton.setOnClickListener {
            onBackUserSettingsButtonPressed()
        }

        val saveButtonUserSettings = findViewById<ImageButton>(R.id.saveButtonAccountSettings)
        saveButtonUserSettings.setOnClickListener{
            val frameSizeValue = findViewById<EditText>(R.id.editTextFrameSize).text.toString()
            val heightValue = findViewById<EditText>(R.id.editTextHeight).text.toString()
            val diskTeethValue = findViewById<EditText>(R.id.editTextDiskTeeth).text.toString()
            val pinionTeethValue = findViewById<EditText>(R.id.editTextPinionTeeth).text.toString()
            val stemValue = findViewById<EditText>(R.id.editTextStem).text.toString()
            PresenterSettings.getInstance(this).onSaveButtonUserSettingsPressed(frameSizeValue,heightValue,
                diskTeethValue,pinionTeethValue,stemValue)
        }

        PresenterSettings.getInstance(this).updateBikeSettings(
            findViewById<EditText>(R.id.editTextFrameSize),
            findViewById<EditText>(R.id.editTextHeight),
            findViewById<EditText>(R.id.editTextDiskTeeth),
            findViewById<EditText>(R.id.editTextPinionTeeth),
            findViewById<EditText>(R.id.editTextStem)


        )
    }

    private fun onSaveButtonUserSettingsPressed() {
       /* val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        //val intent = Intent(application, ViewSettings().javaClass)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //startActivity(intent)
        onBackPressed()
    }
}
