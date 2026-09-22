package com.example.mausam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mausam.utils.AppPreferences

data class LanguageOption(
    val code: String,
    val nativeName: String,
    val englishName: String
)

val languages = listOf(
    LanguageOption("en", "EN", "English"),
    LanguageOption("hi", "हिंदी", "Hindi"),
    LanguageOption("bn", "বাংলা", "Bengali"),
    LanguageOption("gu", "ગુજરાતી", "Gujarati"),
    LanguageOption("kn", "ಕನ್ನಡ", "Kannada"),
    LanguageOption("mr", "मराठी", "Marathi"),
    LanguageOption("ml", "മലയാളം", "Malayalam"),
    LanguageOption("ta", "தமிழ்", "Tamil"),
    LanguageOption("te", "తెలుగు", "Telugu"),
    LanguageOption("ur", "اردو", "Urdu"),
    LanguageOption("or", "ଓଡ଼ିଆ", "Odia"),
    LanguageOption("pa", "ਪੰਜਾਬੀ", "Punjabi")
)

/**
 * Language selection screen matching Image 1.
 * Features clean top sky artwork, 12 regional language grid without text wrapping,
 * bottom landscape artwork, and "Continue ->" CTA button.
 */
@Composable
fun LanguageScreen(
    onContinueClick: (selectedLanguage: String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedLanguageName by remember {
        mutableStateOf(AppPreferences.getSelectedLanguage(context))
    }

    val logoResId = remember(context) {
        context.resources.getIdentifier("imd_logo", "drawable", context.packageName)
    }
    val bgTopResId = remember(context) {
        context.resources.getIdentifier("bg_language_top", "drawable", context.packageName)
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
                contentDescription = "Language Sky Top Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )
        }

        // Bottom Landscape Artwork (Mountains, water, pine trees)
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Header: IMD Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tricolor Accent Line + Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = if (logoResId != 0) 46.dp else 0.dp)
            ) {
                // Indian Tricolor Bar
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

            Spacer(modifier = Modifier.height(20.dp))

            // Main Title & Subtitle
            Text(
                text = "Choose your language",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B2B52)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Select your preferred language to continue",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4B637D)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 12-Language Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(languages) { lang ->
                    val isSelected = lang.englishName.equals(selectedLanguageName, ignoreCase = true)

                    LanguageCard(
                        language = lang,
                        isSelected = isSelected,
                        onSelect = {
                            selectedLanguageName = lang.englishName
                            AppPreferences.setSelectedLanguage(context, lang.englishName)
                        }
                    )
                }
            }

            // Bottom Tagline & Continue Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xD0FFFFFF))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Column {
                            Text(
                                text = "Accurate weather\nfor a safer India",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0B2B52),
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(28.dp)
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
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Primary "Continue ->" Button
                Button(
                    onClick = {
                        AppPreferences.setSelectedLanguage(context, selectedLanguageName)
                        onContinueClick(selectedLanguageName)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continue Arrow",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageCard(
    language: LanguageOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFE3F0FC) else Color(0xFAF8FCFF)
    val borderColor = if (isSelected) Color(0xFF2B78E4) else Color(0xFFDDE7F2)
    val borderWidth = if (isSelected) 1.5.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = language.nativeName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B2B52),
                    maxLines = 1,
                    softWrap = false
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(16.dp)
                        .background(if (isSelected) Color(0xFFBFD8F5) else Color(0xFFE2ECF6))
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = language.englishName,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Color(0xFF0B2B52) else Color(0xFF4B637D),
                    maxLines = 1,
                    softWrap = false
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0B2B52)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun LanguageScreenPreview() {
    Surface {
        LanguageScreen()
    }
}
