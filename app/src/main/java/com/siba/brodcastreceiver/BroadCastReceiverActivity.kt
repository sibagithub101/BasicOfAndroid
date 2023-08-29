package com.siba.brodcastreceiver

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.siba.brodcastreceiver.ui.theme.ContentproviderTheme

class BroadCastReceiverActivity : ComponentActivity() {
    private val airplaneModeReceiver = AirplaneModeReceiver()
    private val textReceiver = TextReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        registerReceiver(textReceiver, IntentFilter("TEST_ACTION"))


        setContent {
            ContentproviderTheme {
           Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
               ) {
            Button(onClick = {
                sendBroadcast(Intent("TEST_ACTION")) // send data bradcastReceiver
            }) {
                Text(text = "Send Broadcast")
            }
           }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
        unregisterReceiver(textReceiver)
    }
}

