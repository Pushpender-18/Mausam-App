package com.example.mausam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.utils.TimeUtils
import kotlinx.coroutines.launch

/**
 * Redesigned Unified Onboarding Landing Page.
 * Features a static background (adapts to Day/Night based on system time)
 * with a 3-page HorizontalPager allowing swiping left/right across:
 * - State 0: Branding (Logo, Title, Subtitles, Tricolor Bar, Tagline)
 * - State 1: Weather Warnings (Central alert bell & alert chips)
 * - State 2: Get Accurate Weather (Central smartphone UI & feature chips)
 */
@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val isDay = remember { TimeUtils.isDaytime() }
    val pagerState = rememberPagerState(pageCount = { 3 })

    // Auto scroll sequence from page 0 -> 1 -> 2 with swipe motion and 100ms delay
    LaunchedEffect(Unit) {
        delay(100L)
        pagerState.animateScrollToPage(1, animationSpec = tween(durationMillis = 600))
        delay(100L)
        pagerState.animateScrollToPage(2, animationSpec = tween(durationMillis = 600))
    }

    val buttonColor = if (isDay) Color(0xFF0B2B52) else Color(0xFF2563EB)
    val dotActiveColor = if (isDay) Color(0xFF0B2B52) else Color.White
    val dotInactiveColor = if (isDay) Color(0xFFB0C4DE) else Color(0xFF64748B)

    val bgResId = remember(context, isDay) {
        val preferredName = if (isDay) "bg_day" else "bg_night"
        val fallbackName = if (isDay) "bg_night" else "bg_day"
        var id = context.resources.getIdentifier(preferredName, "drawable", context.packageName)
        if (id == 0) {
            id = context.resources.getIdentifier(fallbackName, "drawable", context.packageName)
        }
        id
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Static Background Layer
        StaticOnboardingBackground(isDay = isDay, bgResId = bgResId)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Swipable 3-State Content Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer {
                        translationY = -size.height * 0.15f
                    }
            ) { page ->
                when (page) {
                    0 -> OnboardingBrandingState(isDay = isDay)
                    1 -> OnboardingWarningsState(isDay = isDay)
                    2 -> OnboardingAccurateWeatherState(isDay = isDay)
                }
            }

            // Fixed Bottom Controls (Get Started Button + 3-Dot Indicator)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Primary Action Button ("Get Started →" goes to Language page)
                Button(
                    onClick = {
                        onGetStartedClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Arrow Forward",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3-Dot Page Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 8.dp else 7.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) dotActiveColor else dotInactiveColor)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Static Background layer for Onboarding page.
 */
@Composable
private fun StaticOnboardingBackground(isDay: Boolean, bgResId: Int) {
    val context = LocalContext.current
    val waveResId = remember(context) {
        context.resources.getIdentifier("bg_bottom_wave", "drawable", context.packageName)
    }

    val skyBlueColor = if (isDay) Color(0xFFD6EBF8) else Color(0xFF071329)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(skyBlueColor)
    ) {
        // Landscape Background Layer (Shifted 25% upwards - up by additional 15%)
        if (bgResId != 0) {
            Image(
                painter = painterResource(id = bgResId),
                contentDescription = "Static Landscape Background",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = -size.height * 0.25f
                    },
                contentScale = ContentScale.Crop
            )
        }

        // Bottom Wavy Contour Topography Layer (Shifted 15% upwards)
        if (waveResId != 0) {
            Image(
                painter = painterResource(id = waveResId),
                contentDescription = "Bottom Wavy Topography",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = -size.height * 0.15f
                    },
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

/**
 * State 0: Branding (Logo, Title, Subtitles, Tricolor Bar, Tagline)
 */
@Composable
private fun OnboardingBrandingState(isDay: Boolean) {
    val context = LocalContext.current
    val logoResId = remember(context) {
        context.resources.getIdentifier("imd_logo", "drawable", context.packageName)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // IMD Emblem Logo (1.5x bigger: 210dp)
        if (logoResId != 0) {
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = "IMD Emblem Logo",
                modifier = Modifier.size(210.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(if (isDay) Color(0x100A2E5C) else Color(0x1AFFFFFF))
                    .border(
                        1.5.dp,
                        if (isDay) Color(0x300A2E5C) else Color(0x40FFFFFF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMD Logo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDay) Color(0xFF0A2E5C) else Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // App Title
        Text(
            text = "Mausam",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDay) Color(0xFF0B2B52) else Color.White,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitles
        Text(
            text = "India Meteorological Department",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDay) Color(0xFF1E3A8A) else Color(0xFFE2E8F0),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Ministry of Earth Sciences",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = if (isDay) Color(0xFF2563EB) else Color(0xFFCBD5E1),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Indian Flag Tricolor Accent Bar
        Row(
            modifier = Modifier
                .height(4.dp)
                .width(44.dp)
                .clip(RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFFFF9933))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFF138808))
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tagline
        Text(
            text = "A Safer, Weather Ready India",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = if (isDay) Color(0xFF1E3A8A) else Color(0xFFE2E8F0),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * State 1: Weather Warnings (Central Bell Graphic & Alert Badges)
 */
@Composable
private fun OnboardingWarningsState(isDay: Boolean) {
    val context = LocalContext.current
    val bellResId = remember(context) {
        context.resources.getIdentifier("weather_alert_bell", "drawable", context.packageName)
    }
    val heavyRainResId = remember(context) {
        context.resources.getIdentifier("heavy_rainfall_alert", "drawable", context.packageName)
    }
    val heatwaveResId = remember(context) {
        context.resources.getIdentifier("heatwave_alert", "drawable", context.packageName)
    }
    val cycloneResId = remember(context) {
        context.resources.getIdentifier("cyclone_alert", "drawable", context.packageName)
    }
    val strongWindsResId = remember(context) {
        context.resources.getIdentifier("strong_winds_alert", "drawable", context.packageName)
    }
    val dotLineResId = remember(context) {
        context.resources.getIdentifier("dot_line", "drawable", context.packageName)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Orbital Alert Graphic Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            if (dotLineResId != 0) {
                Image(
                    painter = painterResource(id = dotLineResId),
                    contentDescription = "Orbital Dotted Line",
                    modifier = Modifier.size(260.dp),
                    contentScale = ContentScale.Fit
                )
            }

            if (bellResId != 0) {
                Image(
                    painter = painterResource(id = bellResId),
                    contentDescription = "Weather Alert Bell",
                    modifier = Modifier.size(110.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = "🔔",
                    fontSize = 60.sp
                )
            }

            // Top Left Chip: Heavy Rainfall Alert
            if (heavyRainResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 12.dp, top = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = heavyRainResId),
                        contentDescription = "Heavy Rainfall Alert",
                        modifier = Modifier.height(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Top Right Chip: Heatwave Alert
            if (heatwaveResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp, top = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = heatwaveResId),
                        contentDescription = "Heatwave Alert",
                        modifier = Modifier.height(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Bottom Left Chip: Cyclone Alert
            if (cycloneResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = cycloneResId),
                        contentDescription = "Cyclone Alert",
                        modifier = Modifier.height(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Bottom Right Chip: Strong Winds Alert
            if (strongWindsResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = strongWindsResId),
                        contentDescription = "Strong Winds Alert",
                        modifier = Modifier.height(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Titles & Subtitles
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = if (isDay) Color(0xFF0B2B52) else Color.White)) {
                    append("Weather\n")
                }
                withStyle(style = SpanStyle(color = if (isDay) Color(0xFF1E3A8A) else Color(0xFF93C5FD))) {
                    append("warnings")
                }
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tricolor bar
        Row(
            modifier = Modifier
                .height(3.dp)
                .width(32.dp)
                .clip(RoundedCornerShape(1.5.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFFFF9933))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xFF138808))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Get timely alerts for severe\nweather near you.",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = if (isDay) Color(0xFF4B5563) else Color(0xFFCBD5E1),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

/**
 * State 2: Get Accurate Weather (Central Smartphone Graphic & Feature Badges)
 */
@Composable
private fun OnboardingAccurateWeatherState(isDay: Boolean) {
    val context = LocalContext.current
    val mobileResId = remember(context) {
        context.resources.getIdentifier("mobile", "drawable", context.packageName)
    }
    val realTimeResId = remember(context) {
        context.resources.getIdentifier("real_time_forecast", "drawable", context.packageName)
    }
    val tempResId = remember(context) {
        context.resources.getIdentifier("temperature_and_feels_like", "drawable", context.packageName)
    }
    val windResId = remember(context) {
        context.resources.getIdentifier("wind_speed_direction", "drawable", context.packageName)
    }
    val aqResId = remember(context) {
        context.resources.getIdentifier("air_quality", "drawable", context.packageName)
    }
    val severeResId = remember(context) {
        context.resources.getIdentifier("severe_weather_alerts", "drawable", context.packageName)
    }
    val dotLineResId = remember(context) {
        context.resources.getIdentifier("dot_line", "drawable", context.packageName)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Graphic Area (Padded container to ensure surrounding chips are never clipped)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp),
            contentAlignment = Alignment.Center
        ) {
            if (dotLineResId != 0) {
                Image(
                    painter = painterResource(id = dotLineResId),
                    contentDescription = "Orbital Dotted Line",
                    modifier = Modifier.size(270.dp),
                    contentScale = ContentScale.Fit
                )
            }

            if (mobileResId != 0) {
                Image(
                    painter = painterResource(id = mobileResId),
                    contentDescription = "Smartphone Weather App UI",
                    modifier = Modifier.height(210.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = "📱",
                    fontSize = 60.sp
                )
            }

            // Top Left Chip: Real-time Forecasts
            if (realTimeResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 8.dp, top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = realTimeResId),
                        contentDescription = "Real-time Forecasts",
                        modifier = Modifier.height(52.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Top Right Chip: Temperature & Feels Like
            if (tempResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 8.dp, top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = tempResId),
                        contentDescription = "Temperature",
                        modifier = Modifier.height(52.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Middle Left Chip: Wind Speed & Direction
            if (windResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = windResId),
                        contentDescription = "Wind Speed",
                        modifier = Modifier.height(52.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Middle Right Chip: Air Quality (AQI)
            if (aqResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = aqResId),
                        contentDescription = "Air Quality",
                        modifier = Modifier.height(52.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Bottom Right Chip: Severe Weather Alerts
            if (severeResId != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp, bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = severeResId),
                        contentDescription = "Severe Weather Alerts",
                        modifier = Modifier.height(52.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Titles & Subtitles with High-Contrast Navy Color
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF0B2B52))) {
                    append("Get accurate\n")
                }
                withStyle(style = SpanStyle(color = Color(0xFF1D61E0))) {
                    append("weather")
                }
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Get real-time forecasts and\ntimely alerts, tailored to your location.",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF4B637D),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun OnboardingScreenPreview() {
    Surface {
        OnboardingScreen()
    }
}
