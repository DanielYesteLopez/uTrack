package com.example.utrack.model
/*
Guardamos los datos del usuario para poder relacionarlos con los datos de la bicicleta.
 */
class User {
    lateinit var userData:UserData
    lateinit var userBike:UserBike
    fun setUserData(userDataMap: MutableMap<String, String>) {
        val userData = UserData(userDataMap.getValue("email"),
            userDataMap.getValue("name"),
            userDataMap.getValue("real_name"))
    }
}