package com.example.arimagaame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.arimagaame.ui.theme.Arima_GaameTheme
import com.example.arimagaame.ui.theme.ArimaaGameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Arima_GaameTheme {
                // Call your custom composable
                ArimaaGameScreen()
            }
        }
    }
}
