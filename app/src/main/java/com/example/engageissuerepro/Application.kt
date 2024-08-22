package com.example.engageissuerepro

import android.app.Application

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        EngageBroadcastReceiver.register(this)
    }
}