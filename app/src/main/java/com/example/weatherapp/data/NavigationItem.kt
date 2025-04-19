package com.example.weatherapp.data

import androidx.annotation.DrawableRes
import com.example.wpinterviewpractice.R

data class NavigationItem(
    @DrawableRes val icon: Int,
    val route: String,
    val text: String
) {

    companion object {
        fun getNavigationItems(): List<NavigationItem> {
            return listOf(
                NavigationItem(
                    icon = R.drawable.ic_forecast,
                    route = ScreenRoute.FORECAST.route,
                    text = "My forecast",
                ),
                NavigationItem(
                    icon = R.drawable.ic_current_weather,
                    route = ScreenRoute.CURRENT.route,
                    text = "Location",
                ),
                NavigationItem(
                    icon = R.drawable.ic_historical_forecast,
                    route = ScreenRoute.HISTORICAL.route,
                    text = "Historical",
                ),
                NavigationItem(
                    icon = R.drawable.ic_bookmarks,
                    route = ScreenRoute.BOOKMARKS.route,
                    text = "Saved",
                )
            )
        }
    }

}