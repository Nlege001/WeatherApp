package com.example.weatherapp.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.viewmodel.CurrentWeatherViewModel
import com.example.wpinterviewpractice.R

@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val data = viewModel.currentWeatherResponse.collectAsState().value

    val userInput = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = userInput.value,
            onValueChange = {
                userInput.value = it
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.getCurrentWeather(userInput.value)
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.getCurrentWeather(userInput.value) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = ""
                    )
                }
            }
        )

        Text(
            text = data.toString()
        )
    }
}