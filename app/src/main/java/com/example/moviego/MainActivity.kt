package com.example.moviego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.moviego.presentation.navgraph.NavGraph
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.MovieGoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.splashCondition
            }
        }
        setContent {
            MovieGoTheme {
                val startDestination = mainViewModel.startDestination
                NavGraph(startDestination = startDestination)
            }
        }
    }
}
