package com.example.utrack.presenters

import android.content.Context

class PresenterAccountSettings private constructor (context : Context) {
    private var con : Context = context
    companion object : SingletonHolder<PresenterAccountSettings, Context>(::PresenterAccountSettings)
}