package com.example.utrack.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Database {
    private var mDataFirebase = FirebaseDatabase.getInstance()
    private var mDatabaseReferenceBikeSettings = mDataFirebase.reference.child("BikeSettings")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun saveDatabaseBikeSettings(userBikeSettingsMap: MutableMap<String, Int>) {

    }

    fun initializeBikeDatabase(userId: String) {
        val currentUserDb = mDatabaseReferenceBikeSettings.child(userId)
        currentUserDb.child("frame_size").setValue(0)
        currentUserDb.child("height").setValue(0)
        currentUserDb.child("disk_teeth").setValue(0)
        currentUserDb.child("pinion_teeth").setValue(0)
    }
}