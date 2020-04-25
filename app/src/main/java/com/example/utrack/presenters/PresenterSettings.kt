package com.example.utrack.presenters

import android.content.Context

class PresenterSettings {
    val presenterMaster = PresenterMaster()
    fun onSaveButtonUserSettingsPressed(applicationContext: Context, frameSizeValue: Int, heightValue: Int, diskTeethValue: Int, pinionTeethValue: Int, stemValue: Int) {
        val userBikeSettingsMap = mutableMapOf<String,Int>(
            "frame_size" to frameSizeValue,
            "height" to heightValue,
            "disk_teeth" to diskTeethValue,
            "pinion_teeth" to pinionTeethValue
        )
        presenterMaster.setBikeSettings(userBikeSettingsMap)
    }
}