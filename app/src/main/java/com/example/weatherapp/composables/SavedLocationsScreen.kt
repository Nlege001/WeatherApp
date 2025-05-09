package com.example.weatherapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.data.SavedLocationData
import com.example.weatherapp.viewmodel.SavedLocationViewModel

@Composable
fun SavedLocationsScreen(
    viewModel: SavedLocationViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getSavedLocations()
    }

    ViewStateCoordinator(
        state = viewModel.savedLocations.collectAsState().value,
        onRefresh = {}
    ) { data ->
        Scaffold(
            topBar = {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        text = "Saved Locations",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        text = "Swipe left/right to remove locations",
                        textAlign = TextAlign.Center,
                        color = Color.LightGray
                    )
                }
            },
            content = { padding ->
                if (data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.headlineMedium,
                            text = "Search and bookmark locations to see them here",
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(
                            items = data,
                            key = { it.locationName }
                        ) {
                            SwipeToDismissItem(
                                item = it,
                                content = {
                                    Column {
                                        BookmarkListItem(
                                            data = it
                                        )
                                        HorizontalDivider()
                                    }
                                },
                                onRemove = {
                                    viewModel.removeLocation(it.locationName)
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookmarkListItem(
    modifier: Modifier = Modifier,
    data: SavedLocationData,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Weather Icon
        GlideImage(
            model = data.iconUrl,
            contentDescription = "Weather icon",
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Middle: Location and temps
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.locationName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Feels like ${data.feelsLike}°C",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            // Right: Temperature
            Text(
                text = "${data.temperature}°C",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = data.time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}
