package com.example.utrack.ic

interface NaviInterface {
    var doubleBackToExitPressedOnce: Boolean
    fun setDoubleBack(bool:Boolean)
    fun getDoubleBack() : Boolean
    fun onBackPressed()
}