package com.example.gastosmicrowearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gastosmicrowearos.presentation.RegisterGastoScreen
import com.example.gastosmicrowearos.presentation.theme.GastosMicroWearOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GastosMicroWearOSTheme {
                RegisterGastoScreen()
            }
        }
    }
}
