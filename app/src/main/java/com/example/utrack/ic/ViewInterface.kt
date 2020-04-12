package com.example.utrack.ic

interface ViewInterface {
    var doubleBackToExitPressedOnce: Boolean
    fun setDoubleBack(bool:Boolean)
    fun getDoubleBack() : Boolean
    fun onBackPressed()
    fun onCreateHideNavBar()
    fun hideNav()
    fun onWindowFocusChanged(hasFocus: Boolean)
}