package com.example.utrack.presenters

import com.example.utrack.model.Facade
import com.example.utrack.model.Session

class PresenterMaster {
    val facade = Facade()
    fun addNewUser(userDataMap: MutableMap<String, String>) {
        facade.setUserData(userDataMap)
    }

    fun setBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {
        facade.setBikeSettings(userBikeSettingsMap)
    }

    fun initializeBikeDatabase(userId: String) {
        facade.initializeBikeDatabase(userId)
    }

    fun addSession(session: Session) {
        facade.addSession(session)
    }

    fun deleteSession(index: Int) {
        facade.deleteSession(index)
    }

    fun deleteAll(){
        facade.deleteAll()
    }

    fun exportSession(path: String) {
        facade.exportSession(path)
    }

    fun getSessionList() : ArrayList<Session>? {
        return facade.getSessionList()
    }

    fun getSession(index : Int) : Session? {
        return facade.getSession(index)
    }
}