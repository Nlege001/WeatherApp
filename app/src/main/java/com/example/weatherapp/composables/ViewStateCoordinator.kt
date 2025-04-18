package com.example.weatherapp.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.data.CallState

@Composable
fun <T> ViewStateCoordinator(
    modifier: Modifier = Modifier,
    state: CallState<T>,
    onRefresh: () -> Unit,
    errorView: @Composable () -> Unit = { ErrorScreen { onRefresh } },
    loadingView: @Composable () -> Unit = { LoadingScreen() },
    emptyContent: @Composable ColumnScope. () -> Unit = {},
    contentView: @Composable (T) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (state) {
            CallState.Loading -> loadingView.invoke()
            is CallState.Content -> contentView.invoke(state.data)
            is CallState.Error -> errorView.invoke()
            CallState.EmptyContent -> emptyContent.invoke(this)
        }
    }
}