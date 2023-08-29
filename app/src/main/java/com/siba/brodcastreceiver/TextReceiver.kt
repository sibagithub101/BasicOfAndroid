package com.siba.brodcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TextReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action=="TEST_ACTION"){
            println("Receive text intent")
        }
    }
}