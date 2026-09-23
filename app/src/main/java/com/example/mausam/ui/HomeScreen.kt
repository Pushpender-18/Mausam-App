package com.example.mausam.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface

/**
 * Main Home Screen displaying the Weather Dashboard.
 */
@Composable
fun HomeScreen(
    onNavigateToRainAlert: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    WeatherDashboardScreen(
        onNavigateToRainAlert = onNavigateToRainAlert,
        onNavigateToSettings = onNavigateToSettings
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen()
    }
}
