package com.siba.foregroundservice

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.siba.foregroundservice.ui.theme.BasicOfAndroidTheme

class ForeGroundServiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),0)
        }
        setContent {
            BasicOfAndroidTheme {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Button(onClick = { Intent(applicationContext,RunningService::class.java).also {
                        it.action = RunningService.Actions.START.toString()
                        startService(it)
                    } }) {
                        Text(text = "Start Run")
                    }

                    Button(onClick = { Intent(applicationContext,RunningService::class.java).also {
                        it.action = RunningService.Actions.STOP.toString()
                        startService(it)
                    } }) {
                        Text(text = "Stop Run")
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    BasicOfAndroidTheme {

    }
}