package com.example.mausam.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.data.HourlyForecastItem
import com.example.mausam.data.MockWeatherRepository
import com.example.mausam.data.WeatherData

/**
 * Weather icon classification enum.
 */
enum class WeatherIconType {
    PARTLY_CLOUDY,
    LIGHT_RAIN,
    MODERATE_RAIN,
    THUNDERSTORM,
    HEAVY_RAIN
}

/**
 * Main Weather Dashboard Screen.
 * Renders real-time weather metrics, AQI badge, smooth temperature trend curve,
 * and hourly forecast slots with wind speed/direction and precipitation probabilities.
 */
@Composable
fun WeatherDashboardScreen(
    weatherData: WeatherData = remember { MockWeatherRepository.getWeatherData() }
) {
    var selectedHourIndex by remember { mutableIntStateOf(1) } // Default 13:00 selected

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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Top Bar: Location Name ("Kharar"), Menu Icon & User Profile Circle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = weatherData.locationName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

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
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // User Profile Action Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x351D61E0))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Profile",
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
                        text = weatherData.airQuality.value.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Central Weather Overview (Condition, Temp Range, Big Temperature Display)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Condition Text ("Clear Night")
                Text(
                    text = weatherData.conditionText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Min ~ Max Temp & Feels Like
                Text(
                    text = "${weatherData.minTempC} ~ ${weatherData.maxTempC}°C   Feels like ${weatherData.feelsLikeC}°C",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF93C5FD),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Big Temperature Display ("22°C")
                Text(
                    text = "${weatherData.tempC}°C",
                    fontSize = 92.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-2).sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Hourly Forecast Section with Temperature Trend Curve Graph
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Temperature Trend Line Canvas Graph Overlay aligned with 5 columns
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val colWidth = width / 5f

                        // Draw Dotted Horizontal Grid Line
                        drawLine(
                            color = Color(0x30FFFFFF),
                            start = Offset(0f, height * 0.55f),
                            end = Offset(width, height * 0.55f),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )

                        // 5 X Anchors for the 5 hourly columns
                        val x0 = colWidth * 0.5f
                        val x1 = colWidth * 1.5f
                        val x2 = colWidth * 2.5f
                        val x3 = colWidth * 3.5f
                        val x4 = colWidth * 4.5f

                        val y0 = height * 0.70f
                        val y1 = height * 0.65f
                        val y2 = height * 0.40f
                        val y3 = height * 0.25f
                        val y4 = height * 0.45f

                        // Draw Dotted Vertical Line to Selected Column (13:00 - x1)
                        drawLine(
                            color = Color(0x50FFFFFF),
                            start = Offset(x1, y1),
                            end = Offset(x1, height),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )

                        // Temperature Trend Smooth Curve Path
                        val path = Path().apply {
                            moveTo(0f, y0)
                            cubicTo(x0, y0, x1 - colWidth * 0.2f, y1, x1, y1)
                            cubicTo(x1 + colWidth * 0.2f, y1, x2 - colWidth * 0.2f, y2, x2, y2)
                            cubicTo(x2 + colWidth * 0.2f, y2, x3 - colWidth * 0.2f, y3, x3, y3)
                            cubicTo(x3 + colWidth * 0.2f, y3, x4 - colWidth * 0.2f, y4, x4, y4)
                            lineTo(width, y4)
                        }

                        // Red Accent Trend Curve Line
                        drawPath(
                            path = path,
                            color = Color(0xFFEF4444),
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Selected Temperature Badge ("32" at 13:00 column - 2nd column)
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // 12:00
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .shadow(elevation = 4.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "32",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0B2B52)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f)) // 14:00
                        // Peak Temperature Marker ("34" at 15:00 column - 4th column)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = "34",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444),
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f)) // 16:00
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 5 Hourly Forecast Columns evenly distributed across screen width
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    weatherData.hourlyForecasts.forEachIndexed { index, item ->
                        val isSelected = index == selectedHourIndex

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            HourlyForecastColumn(
                                item = item,
                                isSelected = isSelected,
                                onSelect = { selectedHourIndex = index }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual Hourly Forecast Column Slot.
 * Equal 20% width slot ensuring crisp, non-wrapping layout for all 5 hourly items.
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
                text = "${item.popPercent}%",
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
            text = "${item.windSpeedKmh} km/h",
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

        // Time Label
        Text(
            text = item.time,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF93C5FD),
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}

/**
 * Custom Vector Weather Condition Icon Composable.
 * Draws compact, crisp weather symbols (Cloud, Sun, Rain drops, Lightning).
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
