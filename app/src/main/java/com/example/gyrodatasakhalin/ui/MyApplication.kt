package com.example.gyrodatasakhalin.ui

import android.app.Application
import com.example.gyrodatasakhalin.utils.ConnectivityReceiver

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectionListener(listener: ConnectivityReceiver.ConnectionReceiverListener){
        ConnectivityReceiver.connectionReceiverListener = listener
    }

    companion object{
        @get:Synchronized
        lateinit var instance: MyApplication
    }
}