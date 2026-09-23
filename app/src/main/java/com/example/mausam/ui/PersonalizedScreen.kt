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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.utils.AppPreferences

data class PersonalCategoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconResName: String,
    val imageResName: String,
    val iconBgColor: Color
)

val personalizedCategories = listOf(
    PersonalCategoryItem(
        id = "health",
        title = "Health & Lifestyle",
        subtitle = "Health, fitness, family",
        iconResName = "health_icon",
        imageResName = "health_image",
        iconBgColor = Color(0xFFFFF0F1)
    ),
    PersonalCategoryItem(
        id = "travel",
        title = "Travel & Mobility",
        subtitle = "Travel, beach, commute",
        iconResName = "travel_icon",
        imageResName = "travel_image",
        iconBgColor = Color(0xFFEEF5FF)
    ),
    PersonalCategoryItem(
        id = "agriculture",
        title = "Agriculture",
        subtitle = "Farming and gardening",
        iconResName = "agriculture_icon",
        imageResName = "agriculture_image",
        iconBgColor = Color(0xFFEFF8F2)
    ),
    PersonalCategoryItem(
        id = "planning",
        title = "Planning",
        subtitle = "Events and outdoor activities",
        iconResName = "planning_icon",
        imageResName = "planning_image",
        iconBgColor = Color(0xFFF3EFFF)
    )
)

/**
 * Personalized Dashboard Selection Screen matching user requirements:
 * - Card titles ("Health & Lifestyle", "Travel & Mobility") and subtitles fully visible without truncation
 * - Non-scrollable cards layout with expanded card container to prevent clipping
 * - Next action circular arrow button rendered as a floating overlay at bottom-right
 * - Top artwork background with opacity gradient (100% left -> 0% right)
 */
@Composable
fun PersonalizedScreen(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Set of selected category IDs
    var selectedCategoryIds by remember {
        mutableStateOf<Set<String>>(AppPreferences.getPersonalizedCategories(context))
    }

    val logoResId = remember(context) {
        context.resources.getIdentifier("imd_logo", "drawable", context.packageName)
    }
    val bgTopResId = remember(context) {
        var id = context.resources.getIdentifier("bg_personalized", "drawable", context.packageName)
        if (id == 0) {
            id = context.resources.getIdentifier("bg_image", "drawable", context.packageName)
        }
        id
    }

    // Outer container using gradient background with relative overlay elements
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
        // Relative Artwork Overlay: Top Landscape Background with Opacity Gradient (Left: 100%, Right: 0%)
        if (bgTopResId != 0) {
            Image(
                painter = painterResource(id = bgTopResId),
                contentDescription = "Personalized Top Artwork Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .align(Alignment.TopEnd)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black,       // 100% opacity at Left
                                    Color.Transparent  // 0% opacity at Right
                                )
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    },
                contentScale = ContentScale.FillWidth
            )
        }

        // Main Vertical Stack
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Relative Header Bar: IMD Branding (Left) & Skip CTA Pill (Right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Branding Block
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (logoResId != 0) {
                        Image(
                            painter = painterResource(id = logoResId),
                            contentDescription = "IMD Emblem",
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Column {
                        Text(
                            text = "Mausam",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B2B52)
                        )
                        Text(
                            text = "India Meteorological Department",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF4A607A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(20.dp)
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
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Weather for a Safer Tomorrow",
                                fontSize = 10.sp,
                                color = Color(0xFF5B708B)
                            )
                        }
                    }
                }

                // Right "Skip" Action Pill Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x28BDE0FE))
                        .border(1.dp, Color(0x401D61E0), RoundedCornerShape(20.dp))
                        .clickable { onSkipClick() }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Skip",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D61E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Headline & Subtitle Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Tailored for\nyour needs",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B2B52),
                    lineHeight = 38.sp,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose what matters to you.\nWe’ll personalize your weather experience.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4B637D),
                    lineHeight = 20.sp
                )
            }

            // Spacing to position card container comfortably lower on screen
            Spacer(modifier = Modifier.height(28.dp))

            // 3. Category Cards Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                personalizedCategories.forEach { category ->
                    val isSelected = selectedCategoryIds.contains(category.id)
                    PersonalizedCategoryCard(
                        category = category,
                        isSelected = isSelected,
                        onToggleSelect = {
                            val updated = selectedCategoryIds.toMutableSet()
                            if (isSelected) {
                                updated.remove(category.id)
                            } else {
                                updated.add(category.id)
                            }
                            selectedCategoryIds = updated
                            AppPreferences.setPersonalizedCategories(context, updated)
                        }
                    )
                }
            }
        }

        // Relative Overlay: Circular Next Action Arrow Button anchored at Bottom-Right over the cards container
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 24.dp, bottom = 20.dp)
                .size(60.dp)
                .shadow(elevation = 6.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF0B2B52))
                .clickable { onNextClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next Page",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Individual Category Card employing relative layout positioning.
 * Icon box & Title/Subtitle given dedicated space to guarantee 100% full text visibility.
 */
@Composable
fun PersonalizedCategoryCard(
    category: PersonalCategoryItem,
    isSelected: Boolean,
    onToggleSelect: () -> Unit
) {
    val context = LocalContext.current

    val iconResId = remember(context, category.iconResName) {
        context.resources.getIdentifier(category.iconResName, "drawable", context.packageName)
    }
    val imageResId = remember(context, category.imageResName) {
        context.resources.getIdentifier(category.imageResName, "drawable", context.packageName)
    }

    val cardBorderColor = if (isSelected) Color(0xFF1D61E0) else Color(0x200B2B52)
    val cardBorderWidth = if (isSelected) 1.8.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1A0B2B52),
                spotColor = Color(0x1A0B2B52)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(cardBorderWidth, cardBorderColor, RoundedCornerShape(20.dp))
            .clickable { onToggleSelect() }
            .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
    ) {
        // 1. Background Layer: Right Graphic Illustration (Width 88dp so it never crowds text)
        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "${category.title} Illustration",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(72.dp)
                    .width(88.dp),
                contentScale = ContentScale.Fit
            )
        }

        // 2. Foreground Layer: Left Icon + Title/Subtitle (Guaranteed 100% full visibility)
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(end = 90.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Rounded Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(category.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                if (iconResId != 0) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = category.title,
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Title & Subtitle Column
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B2B52),
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = category.subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4B637D),
                    maxLines = 2,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun PersonalizedScreenPreview() {
    Surface {
        PersonalizedScreen()
    }
}
