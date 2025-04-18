package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.weatherapp.composables.BottomNavigationBar
import com.example.weatherapp.composables.CurrentWeatherScreen
import com.example.weatherapp.composables.HistoricalWeatherScreen
import com.example.weatherapp.composables.SavedLocationsScreen
import com.example.weatherapp.data.ScreenRoute
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    contentWindowInsets = WindowInsets.systemBars,
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    val graph =
                        navController.createGraph(startDestination = ScreenRoute.CURRENT.route) {
                            composable(route = ScreenRoute.CURRENT.route) {
                                CurrentWeatherScreen()
                            }
                            composable(route = ScreenRoute.FORECAST.route) {
                                // todo
                            }
                            composable(route = ScreenRoute.HISTORICAL.route) {
                                HistoricalWeatherScreen()
                            }
                            composable(route = ScreenRoute.BOOKMARKS.route) {
                                SavedLocationsScreen()
                            }
                        }
                    NavHost(
                        navController,
                        graph = graph,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}