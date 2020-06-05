package com.example.utrack.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Thread.sleep

class Database {
    private var mDataFirebase = FirebaseDatabase.getInstance()
    private var mDatabaseReferenceBikeSettings = mDataFirebase.reference.child("BikeSettings")
    private var mDatabaseReferenceUserSettings=mDataFirebase.reference.child("Users")
    private var mDatabaseReferenceSession = mDataFirebase.reference.child("Sessions")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var checkSession1 = ""
    var checkSession2 = ""
    var checkSession3 = ""
    var listener = object:ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d("recovery","el listener ha ejecutado")
            checkSession1 = dataSnapshot.child("1").value.toString()
            checkSession2 = dataSnapshot.child("2").value.toString()
            checkSession3 = dataSnapshot.child("3").value.toString()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            // ...
        }
    }
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

    fun clearSessions() {
        val currentUserDb = mDatabaseReferenceSession.child(mAuth.currentUser!!.uid)
        currentUserDb.child("1").setValue(0)
        currentUserDb.child("2").setValue(0)
        currentUserDb.child("3").setValue(0)
    }


    fun initializeSessionDatabase(userId: String) {
        val currentUserDb = mDatabaseReferenceSession.child(userId)
        currentUserDb.child("1").setValue(0)
        currentUserDb.child("2").setValue(0)
        currentUserDb.child("3").setValue(0)
    }

    fun addNewSession(newSession:String){
        val currentUserDb = mDatabaseReferenceSession.child(mAuth.currentUser!!.uid)
        currentUserDb.addValueEventListener(listener)
        when {
            checkSession1.equals("0") -> {
                currentUserDb.child("1").setValue(newSession)
            }
            checkSession2.equals("0") -> {
                currentUserDb.child("2").setValue(newSession)
            }
            checkSession3.equals("0") -> {
                currentUserDb.child("3").setValue(newSession)
            }
            else -> {
                currentUserDb.child("1").setValue(newSession)
                currentUserDb.child("2").setValue(checkSession1)
                currentUserDb.child("3").setValue(checkSession2)
            }
        }
    }
    fun getDatabaseSessions() {
        val currentUserDb = mDatabaseReferenceSession.child(mAuth.currentUser!!.uid)
        currentUserDb.addValueEventListener(listener)
    }
}

