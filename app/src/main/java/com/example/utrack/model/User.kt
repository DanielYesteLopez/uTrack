package com.example.utrack.model

import android.util.Log

/*
Guardamos los datos del usuario para poder relacionarlos con los datos de la bicicleta.
 */
class User {
    lateinit var userData:UserData
    lateinit var userBike:UserBike
    fun setUserData(userDataMap: MutableMap<String, String>) {
        userData = UserData(userDataMap.getValue("email"),
            userDataMap.getValue("name"),
            userDataMap.getValue("real_name"))
    }

    fun setBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {
        if (this::userBike.isInitialized){
            userBike.frameSize = userBikeSettingsMap.getValue("frame_size")
            userBike.height = userBikeSettingsMap.getValue("height")
            userBike.diskTeeth = userBikeSettingsMap.getValue("disk_teeth")
            userBike.pinionTeeth = userBikeSettingsMap.getValue("pinion_teeth")
            userBike.stem = userBikeSettingsMap.getValue("stem")
        }else {
            userBike = UserBike(userBikeSettingsMap.getValue("frame_size"),
                userBikeSettingsMap.getValue("height"),
                userBikeSettingsMap.getValue("disk_teeth"),
                userBikeSettingsMap.getValue("pinion_teeth"),
                userBikeSettingsMap.getValue("stem"))
        }
    }
}