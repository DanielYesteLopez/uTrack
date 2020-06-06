package com.example.utrack.presenters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utrack.views.ViewMainPage
import com.example.utrack.views.ViewSignIn
import com.example.utrack.views.ViewSignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PresenterLogin private constructor (context : Context) {
    private var con : Context = context
    companion object : SingletonHolder<PresenterLogin, Context>(::PresenterLogin)
    private var mDataFirebase = FirebaseDatabase.getInstance()
    private var mDatabaseReference = mDataFirebase.reference.child("Users")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun onSignUpToSignInButtonPressed(
        applicationContext: Context,
        userName: String,
        userEmail: String,
        userPassword: String,
        userConfirmPassword: String,
        userRealName: String
    ) {
        if(userName.isNotEmpty()&&userEmail.isNotEmpty()&&userPassword.isNotEmpty()&&userConfirmPassword.isNotEmpty()&&userRealName.isNotEmpty()) {
            when {
                userPassword.length<6 -> {
                    Toast.makeText(applicationContext,"Your password is too short",Toast.LENGTH_SHORT).show()
                }
                userPassword.equals(userConfirmPassword) -> {
                    createNewAccount(applicationContext, userName, userEmail, userPassword, userRealName)
                }
                else -> {
                    Toast.makeText(applicationContext,"Your passwords doesn't match",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(applicationContext,"Please,fill al the data",Toast.LENGTH_LONG).show()
        }

    }

    private fun createNewAccount(
        applicationContext: Context,
        userName: String,
        userEmail: String,
        userPassword: String,
        userRealName: String
    ) {
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task->
            if (task.isSuccessful){
                verifyEmail(applicationContext)
                Log.d("UserCreated", "createUserWithEmail:success")
                val userId = mAuth.currentUser!!.uid
                val currentUserDb = mDatabaseReference.child(userId)
                currentUserDb.child("name").setValue(userName)
                currentUserDb.child("real_name").setValue(userRealName)
                currentUserDb.child("email").setValue(clearEmailForKey(userEmail))
                PresenterMaster.getInstance(con).initializeBikeDatabase(userId)
                PresenterMaster.getInstance(con).initializeSessionDatabase(userId)
                updateUIToSingIn(applicationContext)

            }else{
                Log.d("Error","Error")
                Toast.makeText(applicationContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyEmail(applicationContext: Context) {
        val mUser = mAuth.currentUser
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
        if(signInUsernameLogin.isEmpty() || signInPasswordLogin.isEmpty()){
            Toast.makeText(applicationContext,
                "Please fill all the data",
                Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(signInUsernameLogin,signInPasswordLogin).addOnCompleteListener { task->
                if (task.isSuccessful){
                    setUserData()
                    updateUIToMainPage(applicationContext)
                }else{
                    Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

        }

        }

    }

    private fun setUserData() {
        val userDataMap = mutableMapOf<String,String>()
        val userDatabaseLocation = mDatabaseReference.child(mAuth.currentUser!!.uid)
        userDatabaseLocation.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    userDataMap[it.key.toString()] = it.value.toString()
                }
                PresenterMaster.getInstance(con).addNewUser(userDataMap)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("Oncancelled","Error")
            }
        })
       // Log.d("TestData",userDatabaseLocation.child("name").toString())
    }

    private fun updateUIToMainPage(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun signUpToSignInbutton(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }

    fun clearEmailForKey(userEmail: String): String {
        var clearUserEmail = userEmail.replace(".",",")
        return clearUserEmail
    }

    fun onToSignUpFromSignInButtonPressed(applicationContext: Context) {
        val intent = Intent(applicationContext,
            ViewSignUp().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(applicationContext,intent,null)
    }


}
