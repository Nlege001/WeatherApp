# 🌤️ Weather App

A polished, modern Android weather application that delivers real-time and historical marine forecasts. It supports both **automatic GPS-based weather retrieval** and **manual city search**, along with advanced features like bookmarking, saved locations, and a historical forecast browser.

---

## 🚀 Key Features

### 📍 Location-Based Forecast
- Auto-detects your current location using **Fused Location Provider**
- Reverse-geocodes to show **City + State**
- Fetches marine weather including hourly breakdown and sunrise/sunset data

### 🔍 City Search
- Search for any city manually
- View current weather and forecast for any queried location
- Results display location-specific metadata

### ⭐ Bookmark Locations
- Save favorite locations locally using **Room database**
- View them in the **Saved Locations** tab

### 📌 Saved Locations
- Lists all your bookmarked cities
- Tap to reload weather data instantly

### 📜 Historical Forecasts
- View past marine weather for selected dates
- Filter and browse historical data by hour or day
- Great for planning or analyzing past weather trends

### 💾 Local Caching (Planned)
- Persist last used location using **DataStore**
- Optimized for returning users

---

## 🧰 Tech Stack

| Category              | Tech / Library                              |
|-----------------------|----------------------------------------------|
| **UI**                | Jetpack Compose + Material 3                 |
| **Navigation**        | Jetpack Compose Navigation                   |
| **Location**          | Fused Location Provider (Play Services)      |
| **Permissions**       | Accompanist Permissions                      |
| **Architecture**      | MVVM (ViewModel + StateFlow)                 |
| **Dependency Injection** | Hilt                                   |
| **Networking**        | Retrofit + Moshi                             |
| **Persistence**       | Room (for bookmarks), DataStore (planned)   |
| **Image Loading**     | Glide (Compose integration)                  |
| **Language**          | Kotlin                                       |
| **Min SDK**           | 26                                           |

---

## 🔑 API Access Required

This app uses a **marine weather API** with **premium paywalled features**, including:

- Historical forecast data
- Hourly marine conditions
- Sunrise/sunset details

You’ll need to **provide your own API key** to run the app successfully.

### 🔧 To use your own key:

1. Sign up at [weatherapi.com](https://www.weatherstack.com/) or your provider of choice.
2. Add your API key to your `local.properties`:
3. Note: This APP uses a standard subscription or the auto complete feature will not work

```properties
WEATHER_API_KEY=your_api_key_here
