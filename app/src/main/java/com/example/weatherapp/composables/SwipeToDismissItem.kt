package com.example.weatherapp.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun <T> SwipeToDismissItem(
    item: T,
    content: @Composable () -> Unit,
    onRemove: (T) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            scope.launch {
                delay(1.seconds)
                onRemove(item)
            }
            true
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            val bgColor by animateColorAsState(
                targetValue = Color.LightGray,
                label = ""
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Removed"
                )
            }
        },
    ) { content.invoke() }
}