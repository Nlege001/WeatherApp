package com.example.weatherapp.composables

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.data.ForecastDay
import com.example.weatherapp.data.HourlyWeather
import com.example.weatherapp.viewmodel.MyForecastViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarineForecastScreen(
    viewModel: MyForecastViewModel = hiltViewModel()
) {
    val data = viewModel.forecast.collectAsState().value
    val context = LocalContext.current
    val cityState = rememberSaveable {
        mutableStateOf<Pair<String, String>?>(null)
    }
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    val location = if (permissionState.allPermissionsGranted) {
        rememberUpdatedLocation(context)
    } else {
        null
    }
    LaunchedEffect(location?.value) {
        location?.value?.let {
            cityState.value = getCityAndStateFromLatLon(context, location.value)
            Log.d("LocationLog", "Lat: ${it.latitude}, Lon: ${it.longitude}")
            viewModel.getForecast(
                lat = it.latitude.toString(),
                long = it.longitude.toString()
            )
        }
    }

    ViewStateCoordinator(
        state = data,
        onRefresh = {},
        contentView = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Forecast for ${cityState.value?.first}, ${cityState.value?.second}",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }

                items(it.forecast) { day ->
                    ForecastDayCard(day)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        emptyContent = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium,
                    text = "Please grant app location permissions",
                    textAlign = TextAlign.Center,
                    color = Color.LightGray
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastDayCard(day: ForecastDay) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = LocalDate.parse(day.date).format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "🌅 Sunrise: ${day.astro.sunrise} | 🌇 Sunset: ${day.astro.sunset}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(day.hourly) { hour ->
                    HourlyWeatherCard(hour)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherCard(hour: HourlyWeather) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(width = 120.dp, height = 200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = formatHour(hour.time), style = MaterialTheme.typography.labelMedium)

            GlideImage(
                model = hour.weather_icons.firstOrNull(),
                contentDescription = "Weather icon",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )

            Text(text = "${hour.temperature}°C", style = MaterialTheme.typography.titleMedium)
            Text(
                text = hour.weather_descriptions.firstOrNull() ?: "",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Wind: ${hour.wind_speed} km/h",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

fun formatHour(raw: String): String {
    return raw.padStart(4, '0').let {
        val hour = it.take(2).toInt()
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour % 12 == 0) 12 else hour % 12
        "$displayHour $amPm"
    }
}

@Composable
fun rememberUpdatedLocation(context: Context): State<Location?> {
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationState = remember { mutableStateOf<Location?>(null) }

    DisposableEffect(Unit) {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMaxUpdates(1)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val latest = result.lastLocation
                locationState.value = latest
                fusedClient.removeLocationUpdates(this)
            }
        }

        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        onDispose {
            fusedClient.removeLocationUpdates(callback)
        }
    }

    return locationState
}

fun getCityAndStateFromLatLon(
    context: Context,
    location: Location?
): Pair<String, String>? {
    val lat = location?.latitude
    val long = location?.longitude
    return if (lat != null && long != null) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(lat, long, 1)

            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val city = address.locality ?: address.subAdminArea ?: "Unknown City"
                val state = address.adminArea ?: "Unknown State"
                Log.d("Geocoder", "City: $city, State: $state")
                city to state
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Failed to get address from location", e)
            null
        }
    } else {
        null
    }
}




