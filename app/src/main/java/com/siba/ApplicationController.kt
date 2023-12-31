package com.siba

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ApplicationController:Application() {
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("running_channel","Running Notification",NotificationManager.IMPORTANCE_HIGH)
            val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManger.createNotificationChannel(channel)

        }
    }
}