package com.example.mausam.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.R

/**
 * Settings Screen matching the provided design layout.
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onRainAlertClick: () -> Unit = {},
    onLocationPermissionClick: () -> Unit = {},
) {
    var rainUnit by remember { mutableStateOf("mm") }
    var tempUnit by remember { mutableStateOf("°C") }
    var timeFormat by remember { mutableStateOf("24 Hours") }
    var notification by remember { mutableStateOf("Off") }

    var isRainAlertExpanded by remember { mutableStateOf(value = true) }
    var alarmState by remember { mutableStateOf("On") }
    var intensityState by remember { mutableStateOf("Average") }
    var distanceState by remember { mutableStateOf("20") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image matching setting_bg
        Image(
            painter = painterResource(id = R.drawable.setting_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Settings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            // Scrollable Content List
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Card 1: Rain Unit
                SettingItemCard(
                    iconResId = R.drawable.rain_unit_icon,
                    title = "Rain Unit"
                ) {
                    SegmentedToggle(
                        options = listOf("mm", "in"),
                        selectedOption = rainUnit,
                        onOptionSelected = { rainUnit = it }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 2: Temperature Unit
                SettingItemCard(
                    iconResId = R.drawable.temperature_icon,
                    title = "Temperature Unit"
                ) {
                    SegmentedToggle(
                        options = listOf("°C", "°F"),
                        selectedOption = tempUnit,
                        onOptionSelected = { tempUnit = it }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 3: Time Format
                SettingItemCard(
                    iconResId = R.drawable.time_format_icon,
                    title = "Time Format"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timeFormat,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 4: Notification
                SettingItemCard(
                    iconResId = R.drawable.notification_icon,
                    title = "Notification"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 5: Rain Alert (Expandable Card)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Column {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isRainAlertExpanded = !isRainAlertExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.rain_alert_icon),
                                contentDescription = "Rain Alert",
                                modifier = Modifier.size(42.dp)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Rain Alert",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F172A)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Info Icon (Circle outline with 'i')
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .border(1.2.dp, Color(0xFF2563EB), CircleShape)
                                        .clickable { onRainAlertClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "i",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2563EB),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isRainAlertExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isRainAlertExpanded) "Collapse" else "Expand",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Expanded Options
                        AnimatedVisibility(
                            visible = isRainAlertExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                // Sub-row 1: Alarm
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 56.dp, top = 4.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Alarm",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0F172A)
                                    )

                                    SegmentedToggle(
                                        options = listOf("On", "Off"),
                                        selectedOption = alarmState,
                                        onOptionSelected = { alarmState = it }
                                    )
                                }

                                // Sub-row 2: Intensity
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 56.dp, top = 4.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Intensity",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0F172A)
                                    )

                                    SegmentedToggle(
                                        options = listOf("Average", "Maximum"),
                                        selectedOption = intensityState,
                                        onOptionSelected = { intensityState = it }
                                    )
                                }

                                // Sub-row 3: Distance
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 56.dp, top = 4.dp, bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Distance",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0F172A)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SegmentedToggle(
                                            options = listOf("20", "40"),
                                            selectedOption = distanceState,
                                            onOptionSelected = { distanceState = it }
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = "Km",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF0F172A)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 6: Privacy
                SettingItemCard(
                    iconResId = R.drawable.privacy_icon,
                    title = "Privacy"
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 7: Help and Support
                SettingItemCard(
                    iconResId = R.drawable.help_and_support_icon,
                    title = "Help and Support"
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card 8: Location Permission
                SettingItemCard(
                    iconResId = R.drawable.location_icon,
                    title = "Location Permission",
                    onCardClick = onLocationPermissionClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // IMD Footer Branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.imd_logo),
                        contentDescription = "IMD Emblem",
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Powered by",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF475569)
                        )

                        Text(
                            text = "India Meteorological Department",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(14.dp)
                                    .height(1.dp)
                                    .background(Color(0xFF94A3B8))
                            )

                            Text(
                                text = " Weather for a Safer Tomorrow ",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF475569)
                            )

                            Box(
                                modifier = Modifier
                                    .width(14.dp)
                                    .height(1.dp)
                                    .background(Color(0xFF94A3B8))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Reusable Setting Card row item.
 */
@Composable
fun SettingItemCard(
    iconResId: Int,
    title: String,
    onCardClick: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .then(
                if (onCardClick != null) Modifier.clickable { onCardClick() }
                else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                modifier = Modifier.weight(1f)
            )

            trailingContent()
        }
    }
}

/**
 * Custom Segmented Pill Toggle matching design.
 */
@Composable
fun SegmentedToggle(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFE2E8F0))
            .padding(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF2563EB) else Color.Transparent)
                        .clickable { onOptionSelected(option) }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SettingsScreenPreview() {
    Surface {
        SettingsScreen()
    }
}
