package com.example.utrack.Presenters

import com.example.utrack.Model.Facade

class PresenterMaster {
    val facade = Facade()
    fun addNewUser(userDataMap: MutableMap<String, String>) {
        facade.setUserData(userDataMap)
    }

}