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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Location Screen matching Image 2 ("Enable location").
 * Requests location permission on "Allow Location" click or persists manual choice on "I'll choose manually" click.
 */
@Composable
fun LocationScreen(
    onAllowLocationClick: () -> Unit = {},
    onChooseManuallyClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val bgTopResId = remember(context) {
        context.resources.getIdentifier("bg_location_top", "drawable", context.packageName)
    }
    val imgMapResId = remember(context) {
        var id = context.resources.getIdentifier("img_location_map", "drawable", context.packageName)
        if (id == 0) {
            id = context.resources.getIdentifier("img_location", "drawable", context.packageName)
        }
        id
    }
    val bgBottomResId = remember(context) {
        context.resources.getIdentifier("bg_landscape_bottom", "drawable", context.packageName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEFF7FC),
                        Color(0xFFE8F3FB),
                        Color(0xFFDFEEF8)
                    )
                )
            )
    ) {
        // Top Sky Background Artwork restricted to top margin
        if (bgTopResId != 0) {
            Image(
                painter = painterResource(id = bgTopResId),
                contentDescription = "Location Sky Top Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )
        }

        // Bottom Landscape Artwork (Mountains, lake, pine trees)
        if (bgBottomResId != 0) {
            Image(
                painter = painterResource(id = bgBottomResId),
                contentDescription = "Landscape Bottom Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.Crop
            )
        }

        // Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Central Map Graphic (India map with ripple circles and location pin)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imgMapResId != 0) {
                    Image(
                        painter = painterResource(id = imgMapResId),
                        contentDescription = "Enable Location Map Graphic",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.85f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0x150B2B52))
                            .border(1.5.dp, Color(0x300B2B52), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "img_location_map",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B2B52)
                        )
                    }
                }
            }

            // Titles & Subtitle Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF0B2B52))) {
                            append("Enable ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF1D61E0))) {
                            append("location")
                        }
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Get accurate forecasts, alerts and\npersonalized weather information\nfor your area.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4B637D),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Actions: Allow Location Button + Choose Manually Text Link
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onAllowLocationClick,
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0B2B52),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Pin",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Allow Location",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary Text Link with contrast backdrop pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xD0FFFFFF))
                        .clickable { onChooseManuallyClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "I'll choose manually",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B2B52)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun LocationScreenPreview() {
    Surface {
        LocationScreen()
    }
}
