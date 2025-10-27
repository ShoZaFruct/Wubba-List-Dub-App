package com.example.wubbalistdubapp

import android.app.Application
import com.example.wubbalistdubapp.di.ServiceLocator

class WubbaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
