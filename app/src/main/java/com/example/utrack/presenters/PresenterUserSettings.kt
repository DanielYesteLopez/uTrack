package com.example.utrack.presenters

import android.content.Context

class PresenterUserSettings private constructor (private var context: Context) {
    companion object : SingletonHolder<PresenterUserSettings, Context>(::PresenterUserSettings)
}