package com.samiksha

import android.app.Application
import android.content.Context

class SamikshaApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: SamikshaApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = SamikshaApplication.applicationContext()
    }
}