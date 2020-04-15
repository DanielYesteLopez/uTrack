package com.example.utrack.Presenters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utrack.Views.ViewSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PresenterLogin {
    private var mDataFirebase = FirebaseDatabase.getInstance()
    private var mDatabaseReference = mDataFirebase.reference.child("Users")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun onSignUpToSignInButtonPressed(applicationContext: Context,userName:String,userEmail:String,userPassword:String) {
         createNewAccount(applicationContext,userName,userEmail,userPassword)
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
                updateUIToSingIn(applicationContext)

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

    private fun updateUIToSingIn(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun onToSignInButtonPressed(
        applicationContext: Context,
        signInUsernameLogin: String,
        signInPasswordLogin: String
    ) {
        mAuth.signInWithEmailAndPassword(signInUsernameLogin,signInPasswordLogin).addOnCompleteListener { task->
            if (task.isSuccessful){
                updateUIToMainPage(applicationContext)
            }else{
                Toast.makeText(applicationContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateUIToMainPage(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }



}
