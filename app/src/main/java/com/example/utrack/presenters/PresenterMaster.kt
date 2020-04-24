package com.example.utrack.presenters

import com.example.utrack.model.Facade

class PresenterMaster {
    val facade = Facade()
    fun addNewUser(userDataMap: MutableMap<String, String>) {
        facade.setUserData(userDataMap)
    }

}