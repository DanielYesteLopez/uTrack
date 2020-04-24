package com.example.utrack.model

class Facade {
    val user = User()
    fun setUserData(userDataMap: MutableMap<String, String>) {
        user.setUserData(userDataMap)
    }
}