package com.example.utrack.presenters

import android.content.Context
import android.widget.Toast

class PresenterAccountSettings private constructor (private var context : Context) {
    companion object : SingletonHolder<PresenterAccountSettings, Context>(::PresenterAccountSettings)
    fun changeUserAccount(
        userName: String,
        password: String,
        confirmPassword: String,
        realName: String,
        accountEmail: String,
        applicationContext: Context
    ) {
        if(StringUtils.isAllValid(userName,password,confirmPassword,realName,accountEmail)){
            if (password.equals(confirmPassword)){
                PresenterMaster.getInstance(context).changeUserAccount(userName,password,realName,accountEmail)
                Toast.makeText(context,"Saved!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"Passwords doesn't match", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(applicationContext,"Please fill in all the data", Toast.LENGTH_SHORT).show()
        }
    }
    object StringUtils {

        fun isAllValid(vararg args: String?) : Boolean {

            //checking all strings passed and if a single string is not valid returning false.
            args.forEach {
                if(isNotValid(it))
                    return false
            }
            return true
        }

        fun isValid(string: String?): Boolean {
            return string != null && string.isNotEmpty() && string.isNotBlank()
        }

        fun isNotValid(string: String?) : Boolean {
            return isValid(string).not()
        }

    }
}
