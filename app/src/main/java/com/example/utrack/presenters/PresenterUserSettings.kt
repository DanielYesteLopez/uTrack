package com.example.utrack.presenters

import android.content.Context

class PresenterUserSettings private constructor (context : Context) {
    private var con : Context = context
    companion object : SingletonHolder<PresenterUserSettings, Context>(::PresenterUserSettings)
}