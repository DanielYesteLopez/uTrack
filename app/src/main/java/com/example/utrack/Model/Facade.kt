package com.example.utrack.Model

class Facade {
    val user = User()
    fun setUserData(userDataMap: MutableMap<String, String>) {
        user.setUserData(userDataMap)
    }
}