package com.example.utrack.presenters

import android.content.Context

class PresenterSettings {
    fun onSaveButtonUserSettingsPressed(frameSizeValue: String, heightValue: String, diskTeethValue: String, pinionTeethValue: String, stemValue: String) {
        var setFrameSizeValue = "0"
        var setHeightValue = "0"
        var setDiskTeethValue = "0"
        var setPinionTeethValue = "0"
        var setStemValue = "0"
        when{
            frameSizeValue.isNotEmpty()->
                setFrameSizeValue = frameSizeValue
            heightValue.isNotEmpty()->
                setHeightValue = heightValue
            diskTeethValue.isNotEmpty()->
                setDiskTeethValue = diskTeethValue
            pinionTeethValue.isNotEmpty()->
                setPinionTeethValue = pinionTeethValue
            stemValue.isNotEmpty()->
                setStemValue = stemValue
        }
        val userBikeSettingsMap = mutableMapOf<String,Int>(
            "frame_size" to setFrameSizeValue.toInt(),
            "height" to setHeightValue.toInt(),
            "disk_teeth" to setDiskTeethValue.toInt(),
            "pinion_teeth" to setPinionTeethValue.toInt(),
            "stem" to setStemValue.toInt()
        )
        presenterMaster.setBikeSettings(userBikeSettingsMap)
    }

    val presenterMaster = PresenterMaster()

}