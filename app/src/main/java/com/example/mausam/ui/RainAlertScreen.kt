package com.example.mausam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.R

/**
 * Rain Alert Information Screen displaying preference options
 * and explanation of how rain alerts work in the app.
 */
@Composable
fun RainAlertScreen(
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // 1. Top Bar with Back Action & Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Rain Alert",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        // 2. Scrollable Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Section: Text Left + Illustration Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "How Rain Alerts Work",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "There are four preference options that control how you receive rain alerts in the app.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF64748B),
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.top_image),
                    contentDescription = "How Rain Alerts Work",
                    modifier = Modifier.size(width = 130.dp, height = 100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Preference Card 1: Enable/Disable Alarms
            PreferenceCard(
                badgeNumber = "1",
                badgeBgColor = Color(0xFFE0EDFF),
                badgeTextColor = Color(0xFF2563EB),
                iconResId = R.drawable.alarm_icon,
                title = "Enable/Disable Alarms"
            ) {
                PreferenceDetailRow(
                    label = "Enable",
                    labelColor = Color(0xFF2563EB),
                    description = "User will receive rain alerts as notifications.",
                    labelWidth = 62.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceDetailRow(
                    label = "Disable",
                    labelColor = Color(0xFF0F172A),
                    description = "No notifications for rain alerts.",
                    labelWidth = 62.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preference Card 2: Region of Interest (20km/40km)
            PreferenceCard(
                badgeNumber = "2",
                badgeBgColor = Color(0xFFDCFCE7),
                badgeTextColor = Color(0xFF16A34A),
                iconResId = R.drawable.region_icon,
                title = "Region of Interest (20km/40km)"
            ) {
                PreferenceDetailRow(
                    label = "20 km",
                    labelColor = Color(0xFF16A34A),
                    description = "Alerts for rain will be issued for region within 20 kms of the user location.",
                    labelWidth = 62.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceDetailRow(
                    label = "40 km",
                    labelColor = Color(0xFF16A34A),
                    description = "Alerts for rain will be issued for region within 40 kms of the user location.",
                    labelWidth = 62.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preference Card 3: Intensity type (Average/Maximum)
            PreferenceCard(
                badgeNumber = "3",
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF9333EA),
                iconResId = R.drawable.intensity_icon,
                title = "Intensity type (Average/Maximum)"
            ) {
                PreferenceDetailRow(
                    label = "Average",
                    labelColor = Color(0xFF7C3AED),
                    description = "Average intensity of the rain cloud will be displayed in the alert notification.",
                    labelWidth = 72.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceDetailRow(
                    label = "Maximum",
                    labelColor = Color(0xFF7C3AED),
                    description = "Maximum intensity of the rain cloud will be displayed in the alert notification.",
                    labelWidth = 72.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preference Card 4: Alert method (Distance/Intensity)
            PreferenceCard(
                badgeNumber = "4",
                badgeBgColor = Color(0xFFFFEDD5),
                badgeTextColor = Color(0xFFEA580C),
                iconResId = R.drawable.alert_icon,
                title = "Alert method (Distance/Intensity)"
            ) {
                PreferenceDetailRow(
                    label = "Distance",
                    labelColor = Color(0xFFEA580C),
                    description = "Alert will be issued for the rain cloud closest to the user within the region of Interest.",
                    labelWidth = 72.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceDetailRow(
                    label = "Intensity",
                    labelColor = Color(0xFFEA580C),
                    description = "Alert will be issued for the rain cloud with highest intensity Avg or Max based on your selection in preference 3 within the region of Interest.",
                    labelWidth = 72.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preference Card 5: Default
            PreferenceCard(
                badgeNumber = "5",
                badgeBgColor = Color(0xFFF1F5F9),
                badgeTextColor = Color(0xFF64748B),
                iconResId = R.drawable.default_icon,
                title = "Default"
            ) {
                Text(
                    text = "Alert is based on distance for 20km radius and average intensity.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Tip Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEBF3FE))
                    .border(1.dp, Color(0xFFD0E1FD), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = LightbulbIconVector,
                        contentDescription = "Tip",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFFBFDBFE))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "You can change these preferences anytime from settings.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E40AF),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

/**
 * Reusable Card container for Rain Alert Preference Options.
 */
@Composable
private fun PreferenceCard(
    badgeNumber: String,
    badgeBgColor: Color,
    badgeTextColor: Color,
    iconResId: Int,
    title: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column: Badge Circle & Icon
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(badgeBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeNumber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Right Column: Title & Key-Value Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                content()
            }
        }
    }
}

/**
 * Key-Value detail row inside preference cards with aligned colons.
 */
@Composable
private fun PreferenceDetailRow(
    label: String,
    labelColor: Color,
    description: String,
    labelWidth: Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = labelColor,
            modifier = Modifier.width(labelWidth)
        )

        Text(
            text = " : ",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )

        Text(
            text = description,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF475569),
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Custom Lightbulb Vector Icon.
 */
private val LightbulbIconVector: ImageVector
    get() {
        if (_lightbulbIconVector != null) return _lightbulbIconVector!!
        _lightbulbIconVector = ImageVector.Builder(
            name = "Lightbulb",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF2563EB))) {
                moveTo(12f, 2f)
                curveTo(8.13f, 2f, 5f, 5.13f, 5f, 9f)
                curveTo(5f, 11.38f, 6.19f, 13.47f, 8f, 14.74f)
                verticalLineTo(17f)
                curveTo(8f, 17.55f, 8.45f, 18f, 9f, 18f)
                horizontalLineTo(15f)
                curveTo(15.55f, 18f, 16f, 17.55f, 16f, 17f)
                verticalLineTo(14.74f)
                curveTo(17.81f, 13.47f, 19f, 11.38f, 19f, 9f)
                curveTo(19f, 5.13f, 15.87f, 2f, 12f, 2f)
                close()
                moveTo(9f, 19f)
                horizontalLineTo(15f)
                verticalLineTo(20f)
                curveTo(15f, 20.55f, 14.55f, 21f, 14f, 21f)
                horizontalLineTo(10f)
                curveTo(9.45f, 21f, 9f, 20.55f, 9f, 20f)
                verticalLineTo(19f)
                close()
            }
        }.build()
        return _lightbulbIconVector!!
    }

private var _lightbulbIconVector: ImageVector? = null

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun RainAlertScreenPreview() {
    Surface {
        RainAlertScreen()
    }
}
