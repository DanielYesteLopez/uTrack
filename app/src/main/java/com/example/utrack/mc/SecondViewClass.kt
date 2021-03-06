package com.example.utrack.mc

import android.os.Build
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.utrack.ic.NaviInterface
import com.example.utrack.ic.ViewInterface

abstract class SecondViewClass : AppCompatActivity(),
    ViewInterface, NaviInterface {

    override var doubleBackToExitPressedOnce: Boolean = false

    override fun setDoubleBack(bool:Boolean){
        this.doubleBackToExitPressedOnce = bool
    }

    override fun getDoubleBack() : Boolean {
        return doubleBackToExitPressedOnce
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    override fun onCreateHideNavBar(){
        // hide navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        hideNav()
    }

    override fun hideNav() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }
}