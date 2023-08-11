package com.bullhead.androidequalizer

import android.app.Application

class MainApplication : Application() {

    companion object{
        @JvmStatic
        lateinit var main:Application


    }

    override fun onCreate() {
        super.onCreate()
        main = this
    }

}