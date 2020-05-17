package com.example.utrack.presenters

import android.content.Context

class PresenterAccountSettings private constructor (private var context : Context) {
    companion object : SingletonHolder<PresenterAccountSettings, Context>(::PresenterAccountSettings)
}