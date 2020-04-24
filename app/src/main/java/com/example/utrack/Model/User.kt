package com.example.utrack.Model
/*
Guardamos los datos del usuario para poder relacionarlos con los datos de la bicicleta.
 */
class User {
    lateinit var userData:UserData
    fun setUserData(userDataMap: MutableMap<String, String>) {
        val userData = UserData(userDataMap.getValue("email"),
            userDataMap.getValue("name"),
            userDataMap.getValue("real_name"))
    }
}