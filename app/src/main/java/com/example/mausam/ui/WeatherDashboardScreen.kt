package com.example.mausam.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mausam.data.HourlyForecastItem
import com.example.mausam.data.MockWeatherRepository
import com.example.mausam.data.WeatherData
import com.example.mausam.data.WeatherIconType
import com.example.mausam.data.repository.ImdRepository
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private fun getUserLocationCityName(context: Context): String? {
    return try {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val location = locationManager?.let { lm ->
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } else {
                    lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                }
            }

            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                addresses?.firstOrNull()?.locality
                    ?: addresses?.firstOrNull()?.subAdminArea
                    ?: addresses?.firstOrNull()?.adminArea
            } else null
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Main Weather Dashboard Screen integrated with IMD API (`https://api.imd.gov.in/api/v1/`).
 * Asynchronously fetches live IMD weather observations and forecasts.
 */
@Composable
fun WeatherDashboardScreen(
    initialWeatherData: WeatherData = remember { MockWeatherRepository.getWeatherData() },
    onNavigateToRainAlert: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var weatherData by remember { mutableStateOf(initialWeatherData) }
    var isLoading by remember { mutableStateOf(false) }

    val currentHourIndex = remember {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY).coerceIn(0, 23)
    }

    val repository = remember { ImdRepository() }
    val scrollState = rememberScrollState()

    val itemWidthDp = 76.dp
    val itemWidthPx = with(density) { itemWidthDp.toPx() }
    val totalColumns = weatherData.hourlyForecasts.size.coerceAtLeast(1)

    // Calculate active hour index corresponding to the column positioned 2 hours (1.5 items) from left edge of screen
    val activeHourIndex = remember(scrollState.value, totalColumns, itemWidthPx) {
        if (itemWidthPx > 0f && totalColumns > 0) {
            val calculated = ((scrollState.value + itemWidthPx * 1.5f) / itemWidthPx).toInt()
            calculated.coerceIn(0, totalColumns - 1)
        } else 0
    }

    // Fetch Live User Location & IMD API Weather Data
    LaunchedEffect(Unit) {
        isLoading = true
        val userCityName = withContext(Dispatchers.IO) {
            getUserLocationCityName(context)
        }
        val stationId = repository.getStationIdForCity(userCityName)
        val result = repository.fetchWeatherData(stationId = stationId, resolvedLocationName = userCityName)
        result.onSuccess { data ->
            weatherData = data
        }
        isLoading = false
    }

    // Automatically Scroll to Current Time on Launch (positioning current hour 2 hours from left)
    LaunchedEffect(weatherData.hourlyForecasts) {
        if (weatherData.hourlyForecasts.isNotEmpty() && itemWidthPx > 0f) {
            val targetPx = ((currentHourIndex - 1) * itemWidthPx)
                .coerceIn(0f, scrollState.maxValue.toFloat())
                .toInt()

            scrollState.animateScrollTo(
                value = targetPx,
                animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic)
            )
        }
    }

    // Background Gradient: Dark gradient for night, Smooth bluish gradient for day
    val backgroundGradient = if (weatherData.isDaytime) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2563EB),
                Color(0xFF3B82F6),
                Color(0xFF60A5FA),
                Color(0xFF93C5FD)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF061021),
                Color(0xFF091D3A),
                Color(0xFF0C2950),
                Color(0xFF071428)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Top Bar: Location Name, Menu Icon & Settings Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = weatherData.locationName ?: "-",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (isLoading) {
                        Spacer(modifier = Modifier.width(10.dp))
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Menu Action Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                            .clickable { onNavigateToRainAlert() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu / Rain Alert",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // User Profile / Settings Action Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x351D61E0))
                            .clickable { onNavigateToSettings() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Air Quality Badge (AQI / PM 2.5)
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x20FFFFFF))
                    .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = weatherData.airQuality.pollutantName.split(" ").firstOrNull() ?: "PM",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF93C5FD)
                        )
                        Text(
                            text = weatherData.airQuality.pollutantName.split(" ").getOrNull(1) ?: "2.5",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(18.dp)
                            .background(Color(0x40FFFFFF))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = weatherData.airQuality.value?.toString() ?: "N/A",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Central Weather Overview (Condition, Temp Range, Big Temperature Display)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Condition Text ("Clear Night" or "-")
                Text(
                    text = weatherData.conditionText ?: "-",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Min ~ Max Temp & Feels Like
                val tempRangeText = if (weatherData.minTempC != null && weatherData.maxTempC != null && weatherData.feelsLikeC != null) {
                    "${weatherData.minTempC} ~ ${weatherData.maxTempC}°C   Feels like ${weatherData.feelsLikeC}°C"
                } else if (weatherData.minTempC != null && weatherData.maxTempC != null) {
                    "${weatherData.minTempC} ~ ${weatherData.maxTempC}°C   Feels like -°C"
                } else {
                    "- ~ -°C   Feels like -°C"
                }

                Text(
                    text = tempRangeText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF93C5FD),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Big Temperature Display ("22°C" or "-°C")
                val bigTempText = if (weatherData.tempC != null) "${weatherData.tempC}°C" else "-°C"

                Text(
                    text = bigTempText,
                    fontSize = 92.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-2).sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Horizontally Scrollable 24-Hour Forecast Section
            val totalRowWidthDp = itemWidthDp * totalColumns

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                ) {
                    Column(
                        modifier = Modifier.width(totalRowWidthDp)
                    ) {
                        // Calculate animated active column index for smooth ease-in-out sliding
                        val animatedActiveColIndex by animateFloatAsState(
                            targetValue = activeHourIndex.toFloat(),
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = EaseInOutCubic
                            ),
                            label = "DottedLineColIndex"
                        )

                        // Temperature Trend Line Canvas Graph Overlay across all 24 items
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val width = size.width
                                val height = size.height
                                val colWidth = width / totalColumns.toFloat()

                                // Dotted Horizontal Grid Line
                                drawLine(
                                    color = Color(0x30FFFFFF),
                                    start = Offset(0f, height * 0.55f),
                                    end = Offset(width, height * 0.55f),
                                    strokeWidth = 1.5.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )

                                // Continuous Temperature Trend Curve Path
                                val path = Path()
                                val minTemp = 18f
                                val maxTemp = 36f

                                weatherData.hourlyForecasts.forEachIndexed { i, item ->
                                    val x = (i + 0.5f) * colWidth
                                    val temp = (item.tempC ?: 22).toFloat()
                                    val normTemp = ((temp - minTemp) / (maxTemp - minTemp)).coerceIn(0.1f, 0.9f)
                                    val y = height * 0.85f - normTemp * (height * 0.70f)

                                    if (i == 0) {
                                        path.moveTo(0f, y)
                                        path.lineTo(x, y)
                                    } else {
                                        val prevX = (i - 0.5f) * colWidth
                                        val prevTemp = (weatherData.hourlyForecasts[i - 1].tempC ?: 22).toFloat()
                                        val prevNorm = ((prevTemp - minTemp) / (maxTemp - minTemp)).coerceIn(0.1f, 0.9f)
                                        val prevY = height * 0.85f - prevNorm * (height * 0.70f)

                                        val cX1 = prevX + colWidth * 0.5f
                                        val cX2 = x - colWidth * 0.5f
                                        path.cubicTo(cX1, prevY, cX2, y, x, y)
                                    }
                                }

                                // Draw Red Trend Line
                                drawPath(
                                    path = path,
                                    color = Color(0xFFEF4444),
                                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }

                            // Single Smoothly Sliding White Temperature Badge centered directly on the red curve line
                            val floorIdx = animatedActiveColIndex.toInt().coerceIn(0, totalColumns - 1)
                            val ceilIdx = (floorIdx + 1).coerceIn(0, totalColumns - 1)
                            val frac = (animatedActiveColIndex - floorIdx).coerceIn(0f, 1f)

                            val temp0 = (weatherData.hourlyForecasts.getOrNull(floorIdx)?.tempC ?: 22).toFloat()
                            val temp1 = (weatherData.hourlyForecasts.getOrNull(ceilIdx)?.tempC ?: 22).toFloat()
                            val interpTemp = temp0 + (temp1 - temp0) * frac

                            val normTemp = ((interpTemp - 18f) / (36f - 18f)).coerceIn(0.1f, 0.9f)
                            val canvasHeightPx = with(density) { 60.dp.toPx() }
                            val redLineYPx = canvasHeightPx * 0.85f - normTemp * (canvasHeightPx * 0.70f)

                            val activeBadgeX = with(density) {
                                ((animatedActiveColIndex + 0.5f) * itemWidthPx - 14.dp.toPx()).toDp()
                            }
                            val activeBadgeY = with(density) {
                                (redLineYPx - 14.dp.toPx()).toDp()
                            }

                            Box(
                                modifier = Modifier
                                    .offset(x = activeBadgeX, y = activeBadgeY)
                                    .size(28.dp)
                                    .shadow(elevation = 4.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                val activeTemp = weatherData.hourlyForecasts.getOrNull(activeHourIndex)?.tempC
                                Text(
                                    text = if (activeTemp != null) "$activeTemp" else "-",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0B2B52)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 24 Hourly Forecast Items Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            weatherData.hourlyForecasts.forEachIndexed { index, item ->
                                val isActive = index == activeHourIndex

                                Box(
                                    modifier = Modifier.width(itemWidthDp),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    HourlyForecastColumn(
                                        item = item,
                                        isSelected = isActive,
                                        onSelect = {
                                            coroutineScope.launch {
                                                val targetPx = ((index - 1) * itemWidthPx)
                                                    .coerceIn(0f, scrollState.maxValue.toFloat())
                                                    .toInt()
                                                scrollState.animateScrollTo(
                                                    value = targetPx,
                                                    animationSpec = tween(durationMillis = 600, easing = EaseInOutCubic)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual Hourly Forecast Column Slot.
 */
@Composable
fun HourlyForecastColumn(
    item: HourlyForecastItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSelect() }
            .padding(vertical = 2.dp)
    ) {
        // Weather Icon + Precipitation %
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeatherConditionIcon(
                iconType = item.iconType,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = if (item.popPercent != null) "${item.popPercent}%" else "-",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                softWrap = false
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Wind Breeze Icon
        WindBreezeIcon(modifier = Modifier.size(22.dp, 16.dp))

        Spacer(modifier = Modifier.height(6.dp))

        // Wind Speed
        Text(
            text = if (item.windSpeedKmh != null) "${item.windSpeedKmh} km/h" else "-",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            softWrap = false
        )

        // Wind Direction
        Text(
            text = item.windDirection,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            softWrap = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Time Label with smooth text color and scale animation
        val labelColor by animateColorAsState(
            targetValue = if (isSelected) Color.White else Color(0xFF93C5FD),
            animationSpec = tween(durationMillis = 200),
            label = "TimeLabelColor"
        )

        val labelScale by animateFloatAsState(
            targetValue = if (isSelected) 1.12f else 1.0f,
            animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
            label = "TimeLabelScale"
        )

        Text(
            text = item.time,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = labelColor,
            textAlign = TextAlign.Center,
            softWrap = false,
            modifier = Modifier.graphicsLayer {
                scaleX = labelScale
                scaleY = labelScale
            }
        )
    }
}

/**
 * Custom Vector Weather Condition Icon Composable.
 */
@Composable
fun WeatherConditionIcon(
    iconType: WeatherIconType,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (iconType) {
            WeatherIconType.PARTLY_CLOUDY -> {
                // Sun
                drawCircle(
                    color = Color(0xFFFBBF24),
                    radius = w * 0.28f,
                    center = Offset(w * 0.7f, h * 0.35f)
                )
                // Cloud
                drawRoundRect(
                    color = Color(0xFFE2E8F0),
                    topLeft = Offset(w * 0.1f, h * 0.45f),
                    size = Size(w * 0.7f, h * 0.38f),
                    cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFE2E8F0),
                    radius = w * 0.25f,
                    center = Offset(w * 0.38f, h * 0.45f)
                )
            }

            WeatherIconType.LIGHT_RAIN -> {
                // Cloud
                drawRoundRect(
                    color = Color(0xFF94A3B8),
                    topLeft = Offset(w * 0.12f, h * 0.15f),
                    size = Size(w * 0.76f, h * 0.4f),
                    cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
                )
                drawCircle(
                    color = Color(0xFF94A3B8),
                    radius = w * 0.26f,
                    center = Offset(w * 0.42f, h * 0.22f)
                )
                // Rain drops
                drawLine(
                    color = Color(0xFF60A5FA),
                    start = Offset(w * 0.3f, h * 0.65f),
                    end = Offset(w * 0.22f, h * 0.88f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF60A5FA),
                    start = Offset(w * 0.6f, h * 0.65f),
                    end = Offset(w * 0.52f, h * 0.88f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            WeatherIconType.MODERATE_RAIN -> {
                // Cloud
                drawRoundRect(
                    color = Color(0xFF64748B),
                    topLeft = Offset(w * 0.12f, h * 0.12f),
                    size = Size(w * 0.76f, h * 0.4f),
                    cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
                )
                drawCircle(
                    color = Color(0xFF64748B),
                    radius = w * 0.26f,
                    center = Offset(w * 0.42f, h * 0.18f)
                )
                // 3 Rain drops
                val dropOffset = floatArrayOf(0.25f, 0.5f, 0.75f)
                dropOffset.forEach { xFrac ->
                    drawLine(
                        color = Color(0xFF3B82F6),
                        start = Offset(w * xFrac, h * 0.6f),
                        end = Offset(w * (xFrac - 0.08f), h * 0.88f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            WeatherIconType.THUNDERSTORM -> {
                // Dark Cloud
                drawRoundRect(
                    color = Color(0xFF334155),
                    topLeft = Offset(w * 0.12f, h * 0.1f),
                    size = Size(w * 0.76f, h * 0.38f),
                    cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
                )
                drawCircle(
                    color = Color(0xFF334155),
                    radius = w * 0.26f,
                    center = Offset(w * 0.42f, h * 0.15f)
                )
                // Lightning Bolt
                val lightningPath = Path().apply {
                    moveTo(w * 0.52f, h * 0.45f)
                    lineTo(w * 0.38f, h * 0.68f)
                    lineTo(w * 0.54f, h * 0.68f)
                    lineTo(w * 0.42f, h * 0.95f)
                    lineTo(w * 0.62f, h * 0.6f)
                    lineTo(w * 0.48f, h * 0.6f)
                    close()
                }
                drawPath(path = lightningPath, color = Color(0xFFF59E0B))
            }

            WeatherIconType.HEAVY_RAIN -> {
                // Heavy Rain Dark Cloud
                drawRoundRect(
                    color = Color(0xFF1E293B),
                    topLeft = Offset(w * 0.12f, h * 0.12f),
                    size = Size(w * 0.76f, h * 0.4f),
                    cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
                )
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = w * 0.26f,
                    center = Offset(w * 0.42f, h * 0.18f)
                )
                // 3 Heavy Rain drops
                val dropOffset = floatArrayOf(0.22f, 0.5f, 0.78f)
                dropOffset.forEach { xFrac ->
                    drawLine(
                        color = Color(0xFF60A5FA),
                        start = Offset(w * xFrac, h * 0.6f),
                        end = Offset(w * (xFrac - 0.1f), h * 0.9f),
                        strokeWidth = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

/**
 * Custom Breeze/Wind Lines Vector Composable.
 */
@Composable
fun WindBreezeIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        val color = Color(0xFF93C5FD)

        // Line 1
        drawLine(
            color = color,
            start = Offset(0f, h * 0.25f),
            end = Offset(w * 0.85f, h * 0.25f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Line 2
        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.55f),
            end = Offset(w, h * 0.55f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Line 3
        drawLine(
            color = color,
            start = Offset(w * 0.05f, h * 0.85f),
            end = Offset(w * 0.7f, h * 0.85f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun WeatherDashboardScreenPreview() {
    Surface {
        WeatherDashboardScreen()
    }
}
