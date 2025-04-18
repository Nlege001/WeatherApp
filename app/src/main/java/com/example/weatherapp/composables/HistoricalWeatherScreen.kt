package com.example.weatherapp.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.data.HourlyWeather
import com.example.weatherapp.data.Interval
import com.example.weatherapp.viewmodel.HistoricalWeatherViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoricalWeatherScreen(
    viewModel: HistoricalWeatherViewModel = hiltViewModel()
) {
    val autoComplete = viewModel.autoCompleteResponse.collectAsState().value
    val userInput = rememberSaveable { mutableStateOf("") }
    val interval = rememberSaveable { mutableStateOf<Interval?>(null) }
    val date = rememberSaveable { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(
        userInput.value, interval.value, date.value
    ) {
        val selectedInterval = interval.value
        val selectedDate = date.value
        if (selectedInterval != null && selectedDate != null) {
            viewModel.getHistoricalData(
                userInput = userInput.value,
                date = selectedDate,
                interval = selectedInterval
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val expanded = rememberSaveable { mutableStateOf(false) }
        SearchBar(
            modifier = Modifier.padding(bottom = 8.dp),
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
                    placeholder = { androidx.compose.material.Text("Search") }
                )
            },
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it },
            content = {
                autoComplete?.results?.let { results ->
                    if (results.isNotEmpty()) {
                        results.forEach { item ->
                            ListItem(
                                headlineContent = { androidx.compose.material.Text(text = "${item.name}, ${item.country}") },
                                modifier = Modifier
                                    .clickable {
                                        userInput.value = item.name
                                        expanded.value = false
                                    }
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

            }
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Intervals
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Interval", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))

                MultiDatePicker {
                    date.value = it
                }
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(Interval.entries) { option ->
                        val isSelected = interval.value == option
                        Card(
                            modifier = Modifier
                                .clickable {
                                    interval.value = option
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = option.label,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ViewStateCoordinator(
                    state = viewModel.historicalData.collectAsState().value,
                    onRefresh = {}
                ) { data ->
                    val current = data.current
                    val location = data.location
                    val historicalDay = data.historical?.values

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Location + Current Overview
                        Text(
                            text = "${location.name}, ${location.country}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = data.current.weather_descriptions.firstOrNull() ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Weather Icon + Temp
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            GlideImage(
                                model = current.weather_icons.firstOrNull(),
                                contentDescription = "Weather Icon",
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${current.temperature}°C",
                                    style = MaterialTheme.typography.displaySmall
                                )
                                Text(
                                    text = "Feels like ${current.feelslike}°C",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Stats
                        StatRow(label = "Humidity", value = "${current.humidity}%")
                        StatRow(
                            label = "Wind",
                            value = "${current.wind_speed} km/h ${current.wind_dir}"
                        )
                        StatRow(label = "Pressure", value = "${current.pressure} hPa")
                        StatRow(label = "Visibility", value = "${current.visibility} km")
                        StatRow(label = "UV Index", value = "${current.uv_index}")

                        Spacer(modifier = Modifier.height(24.dp))

                        // Historical Section
                        historicalDay?.let {
                            it.forEach {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Historical Weather: ${it.date}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Avg Temp: ${it.avgtemp}°C | Sun Hours: ${it.sunhour}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    items(it.hourly) { hour ->
                                        HourlyCard(hour)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiDatePicker(
    modifier: Modifier = Modifier,
    onDatesSelected: (List<String>) -> Unit
) {
    val sheetState = rememberUseCaseState()
    val selectedDates = remember { mutableStateOf<List<LocalDate>>(emptyList()) }
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    CalendarDialog(
        state = sheetState,
        selection = CalendarSelection.Dates { dates ->
            selectedDates.value = dates
            onDatesSelected(dates.map { it.format(formatter) })
        }
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { sheetState.show() }) {
            Text("Pick Dates")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedDates.value.isEmpty()) {
            Text(
                text = "No dates selected",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = selectedDates.value.joinToString("; ") {
                    it.format(
                        DateTimeFormatter.ofPattern(
                            "MMM dd/yyyy"
                        )
                    )
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value, textAlign = TextAlign.End)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyCard(hour: HourlyWeather) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${hour.time}h", fontWeight = FontWeight.Bold)

            GlideImage(
                model = hour.weather_icons.firstOrNull(),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Text(text = "${hour.temperature}°C")
            Text(
                text = hour.weather_descriptions.firstOrNull() ?: "",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
