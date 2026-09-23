package com.example.mausam

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mausam.ui.HomeScreen
import com.example.mausam.ui.LanguageScreen
import com.example.mausam.ui.LocationScreen
import com.example.mausam.ui.OnboardingScreen
import com.example.mausam.ui.PersonalizedScreen
import com.example.mausam.utils.AppPreferences

enum class AppScreen {
    ONBOARDING,
    LANGUAGE,
    LOCATION,
    PERSONALIZED,
    HOME
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MausamAppContent()
                }
            }
        }
    }
}

@Composable
fun MausamAppContent() {
    val context = LocalContext.current
    val initialScreen = remember(context) {
        if (AppPreferences.isOnboardingCompleted(context)) {
            AppScreen.HOME
        } else {
            AppScreen.ONBOARDING
        }
    }

    var currentScreen by remember { mutableStateOf(initialScreen) }

    // Launcher for Android Location Permissions
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            AppPreferences.setLocationChoice(context, "ALLOWED")
        } else {
            AppPreferences.setLocationChoice(context, "DENIED")
        }
        currentScreen = AppScreen.PERSONALIZED
    }

    when (currentScreen) {
        AppScreen.ONBOARDING -> {
            OnboardingScreen(
                onGetStartedClick = {
                    currentScreen = AppScreen.LANGUAGE
                }
            )
        }

        AppScreen.LANGUAGE -> {
            LanguageScreen(
                onContinueClick = { selectedLang ->
                    AppPreferences.setSelectedLanguage(context, selectedLang)
                    currentScreen = AppScreen.LOCATION
                }
            )
        }

        AppScreen.LOCATION -> {
            LocationScreen(
                onAllowLocationClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                onChooseManuallyClick = {
                    AppPreferences.setLocationChoice(context, "MANUAL")
                    currentScreen = AppScreen.PERSONALIZED
                }
            )
        }

        AppScreen.PERSONALIZED -> {
            PersonalizedScreen(
                onNextClick = {
                    AppPreferences.setOnboardingCompleted(context, true)
                    currentScreen = AppScreen.HOME
                },
                onSkipClick = {
                    AppPreferences.setOnboardingCompleted(context, true)
                    currentScreen = AppScreen.HOME
                }
            )
        }

        AppScreen.HOME -> {
            HomeScreen()
        }
    }
}
