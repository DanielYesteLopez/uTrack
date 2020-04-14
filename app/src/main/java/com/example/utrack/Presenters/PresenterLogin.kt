package com.example.utrack.Presenters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utrack.Views.ViewData
import com.example.utrack.Views.ViewSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PresenterLogin {
    var mDataFirebase = FirebaseDatabase.getInstance()
    var mDatabaseReference = mDataFirebase.reference.child("Users")
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun onSignUpToSignInButtonPressed(applicationContext: Context,userName:String,userEmail:String,userPassword:String) {
         createNewAccount(applicationContext,userName,userEmail,userPassword)
        /*val intent = Intent(application, ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)*/
    }

    private fun createNewAccount(
        applicationContext: Context,
        userName: String,
        userEmail: String,
        userPassword: String
    ) {
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task->
            if (task.isSuccessful){
                verifyEmail(applicationContext)
                Log.d("UserCreated", "createUserWithEmail:success")
                val userId = mAuth.currentUser!!.uid
                val currentUserDb = mDatabaseReference.child(userId)
                currentUserDb.child("name").setValue(userName)
                updateUI(applicationContext)

            }else{
                Log.d("Error","Error")
                Toast.makeText(applicationContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyEmail(applicationContext: Context) {
        val mUser = mAuth.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,
                        "Verification email sent to " + mUser.email,
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

}
