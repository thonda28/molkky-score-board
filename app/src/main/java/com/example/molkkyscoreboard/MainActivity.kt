package com.example.molkkyscoreboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.molkkyscoreboard.ui.theme.MolkkyScoreBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MolkkyScoreBoardTheme {
                ScoreBoardApp()
            }
        }
    }
}
