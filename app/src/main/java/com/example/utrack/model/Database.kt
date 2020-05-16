package com.example.utrack.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Database {
    private var mDataFirebase = FirebaseDatabase.getInstance()
    private var mDatabaseReferenceBikeSettings = mDataFirebase.reference.child("BikeSettings")
    private var mDatabaseReferenceUserSettings=mDataFirebase.reference.child("Users")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun saveDatabaseBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {
        val currentUserDb = mDatabaseReferenceBikeSettings.child(mAuth.currentUser!!.uid)
        currentUserDb.child("frame_size").setValue(userBikeSettingsMap.getValue("frame_size"))
        currentUserDb.child("height").setValue(userBikeSettingsMap.getValue("height"))
        currentUserDb.child("disk_teeth").setValue(userBikeSettingsMap.getValue("disk_teeth"))
        currentUserDb.child("pinion_teeth").setValue(userBikeSettingsMap.getValue("pinion_teeth"))
        currentUserDb.child("stem").setValue(userBikeSettingsMap.getValue("stem"))
    }

    fun initializeBikeDatabase(userId: String) {
        val currentUserDb = mDatabaseReferenceBikeSettings.child(userId)
        currentUserDb.child("frame_size").setValue(0)
        currentUserDb.child("height").setValue(0)
        currentUserDb.child("disk_teeth").setValue(0)
        currentUserDb.child("pinion_teeth").setValue(0)
        currentUserDb.child("stem").setValue(0)
    }

    fun changeUserAccount(userName: String, password: String, realName: String, accountEmail: String) {
        val currentUserDb = mDatabaseReferenceUserSettings.child(mAuth.currentUser!!.uid)
        mAuth.currentUser!!.updateEmail(accountEmail)
        mAuth.currentUser!!.updatePassword(password)
        currentUserDb.child("email").setValue(clearEmailForKey(accountEmail))
        currentUserDb.child("name").setValue(userName)
        currentUserDb.child("real_name").setValue(realName)


    }

    fun clearEmailForKey(userEmail: String): String {
        var clearUserEmail = userEmail.replace(".",",")
        return clearUserEmail
    }
}