package com.example.utrack.presenters

import android.content.Context
import android.widget.EditText
import android.widget.Toast

class PresenterSettings private constructor (private var context: Context) {
    companion object : SingletonHolder<PresenterSettings, Context>(::PresenterSettings)
    fun onSaveButtonUserSettingsPressed(frameSizeValue: String, heightValue: String, diskTeethValue: String, pinionTeethValue: String, stemValue: String) {
        var setFrameSizeValue = "0"
        var setHeightValue = "0"
        var setDiskTeethValue = "0"
        var setPinionTeethValue = "0"
        var setStemValue = "0"
        val stringList = listOf(frameSizeValue,heightValue,diskTeethValue,pinionTeethValue,stemValue)
        for(i in stringList.indices){
            if(i == 0 && stringList[i].isNotEmpty()){
                setFrameSizeValue = frameSizeValue
            }
            if(i == 1 && stringList[i].isNotEmpty()){
                setHeightValue = heightValue

            }
            if(i == 2 && stringList[i].isNotEmpty()){
                setDiskTeethValue = diskTeethValue
            }
            if(i == 3 && stringList[i].isNotEmpty()){
                setPinionTeethValue = pinionTeethValue
            }
            if(i == 4 && stringList[i].isNotEmpty()){
                setStemValue = stemValue
            }
        }
        Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()

        val userBikeSettingsMap = mutableMapOf<String,Int>(
            "frame_size" to setFrameSizeValue.toInt(),
            "height" to setHeightValue.toInt(),
            "disk_teeth" to setDiskTeethValue.toInt(),
            "pinion_teeth" to setPinionTeethValue.toInt(),
            "stem" to setStemValue.toInt()
        )
        PresenterMaster.getInstance(context).setBikeSettings(userBikeSettingsMap)
    }

    fun updateBikeSettings(
        findViewById: EditText,
        findViewById1: EditText,
        findViewById2: EditText,
        findViewById3: EditText,
        findViewById4: EditText
    ) {
        PresenterMaster.getInstance(context).updateBikeSettings(
            findViewById,
            findViewById1,
            findViewById2,
            findViewById3,
            findViewById4
        )
    }
}