package com.marcingantkowski.tolkienquizapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marcingantkowski.tolkienquizapp.presentation.navigation.NavGraph
import com.marcingantkowski.tolkienquizapp.ui.theme.TolkienQuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TolkienQuizAppTheme {
                NavGraph()
            }
        }
    }
}