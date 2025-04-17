package com.example.weatherapp.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.viewmodel.CurrentWeatherViewModel

@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val data = viewModel.currentWeatherResponse.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = data.toString()
        )
    }
}