package com.example.weatherapp.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.data.Current
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.viewmodel.CurrentWeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val data = viewModel.currentWeatherResponse.collectAsState().value
    val autoComplete = viewModel.autoCompleteResponse.collectAsState().value

    val userInput = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val expanded = rememberSaveable { mutableStateOf(false) }
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = userInput.value,
                    onQueryChange = {
                        userInput.value = it
                        viewModel.getAutoCompleteResponse(userInput.value)
                    },
                    onSearch = {
                        viewModel.getAutoCompleteResponse(it)
                        expanded.value = false
                    },
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it },
            content = {
                autoComplete?.results?.let { results ->
                    if (results.isNotEmpty()) {
                        results.forEach { item ->
                            ListItem(
                                headlineContent = { Text(text = "${item.name}, ${item.country}") },
                                modifier = Modifier
                                    .clickable {
                                        userInput.value = item.name
                                        viewModel.getCurrentWeather(userInput.value)
                                        expanded.value = false
                                    }
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

            }
        )

        ViewStateCoordinator(
            state = data,
            onRefresh = { viewModel.getCurrentWeather(userInput.value) }
        ) {
            WeatherScreen(it)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherScreen(
    data: WeatherResponse
) {
    val current = data.current
    val location = data.location

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Location and Time
        Text(
            text = location.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${location.localtime} • ${current.weather_descriptions.firstOrNull() ?: ""}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Icon and Temp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                model = current.weather_icons.firstOrNull(),
                contentDescription = "Weather icon",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${current.temperature}°C",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Feels like ${current.feelslike}°C",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Additional Info
        WeatherStats(current)
    }
}

@Composable
fun WeatherStats(current: Current) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        val rows = listOf(
            "Humidity" to "${current.humidity}%",
            "Wind" to "${current.wind_speed} km/h ${current.wind_dir}",
            "Pressure" to "${current.pressure} hPa",
            "UV Index" to current.uv_index.toString(),
            "Visibility" to "${current.visibility} km"
        )

        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label)
                Text(text = value, textAlign = TextAlign.End)
            }
        }
    }
}
