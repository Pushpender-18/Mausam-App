# Mausam App - Technical Documentation & Working Guide

Welcome to the **Mausam** application codebase documentation. This document provides a complete technical guide on the architecture, onboarding landing page, language selection screen, location permission screen, sequence flows, persistent storage, image asset management, and testing procedures.

---

## 📌 1. Onboarding Flow & Screen Sequence

The app features a streamlined onboarding sequence:

```
[Onboarding Landing Page] ── ("Get Started" / Auto-Scroll 0->1->2) ──> [LanguageScreen] ── Continue ──> [LocationScreen] ── Allow/Manual ──> [HomeScreen]
```

### Screen Details:

1. **Onboarding Landing Page (`OnboardingScreen.kt`)**:
   - **Static Background**: The landscape background remains fixed across all 3 swipable states and automatically adapts to Day/Night theme based on `TimeUtils.isDaytime()`.
   - **Auto-Scroll & Swipe Motion**: Upon screen launch, the `HorizontalPager` automatically scrolls smoothly from State 0 to State 1, and then to State 2 with a swipe motion animation and 100ms delay between states. Manual swiping remains supported.
   - **Page States**:
     - **State 0 (Branding)**: Official IMD emblem/logo, `"Mausam"` title, subtitles (*India Meteorological Department*, *Ministry of Earth Sciences*), Indian Flag Tricolor Accent Bar, and tagline (*A Safer, Weather Ready India*).
     - **State 1 (Weather Warnings)**: Central Bell graphic with orbital dashed ring + 4 alert chips (*Heavy Rainfall Alert*, *Heatwave Alert*, *Cyclone Alert*, *Strong Winds Alert*), title `"Weather warnings"`.
     - **State 2 (Get Accurate Weather)**: Central Smartphone UI graphic with orbital dashed ring + 5 padded feature chips surrounding the phone (*Real-time Forecasts*, *Temperature*, *Wind Speed*, *Air Quality*, *Severe Weather Alerts*) sized at `52dp` with padded bounds to eliminate screen edge clipping. High-contrast navy typography (`#0B2B52` / `#4B637D`).
   - **Primary Action Button**: Labeled `"Get Started →"`. Pressing `"Get Started"` navigates directly to `LanguageScreen`.
   - **No Skip Action**: Skip button has been removed from the onboarding page for an intentional, guided onboarding flow.

2. **Language Selection Page (`LanguageScreen.kt`)**:
   - **Artwork Layers**: Top sky background (`bg_language_top.png` restricted to top `150dp` margin to prevent yellow sun overlap) and bottom landscape background (`bg_landscape_bottom.png`).
   - **Header & Branding**: IMD emblem logo, `"Mausam"`, `"India Meteorological Department"`, Indian Flag Tricolor Accent Bar, and `"Weather for a Safer Tomorrow"` tagline.
   - **Language Grid**: 12 Indian regional languages in a 2-column grid without text wrapping (English, Hindi, Bengali, Gujarati, Kannada, Marathi, Malayalam, Tamil, Telugu, Urdu, Odia, Punjabi).
   - **Selection Indicator**: Active card highlights with light blue background, blue border, and a dark navy checkmark circle icon (`Check`).
   - **Bottom CTA**: Left tagline `"Accurate weather for a safer India"` with high-contrast white backdrop card + tricolor accent line + dark navy `"Continue →"` pill button. Persists choice to `AppPreferences` and navigates to `LocationScreen`.

3. **Location Permission Page (`LocationScreen.kt`)**:
   - **Artwork Layers**: Top sky background (`bg_location_top.png` restricted to top `150dp` margin to prevent sun overlap with map), central India map graphic (`img_location_map.png` with ripple circles and location pin), and bottom landscape artwork (`bg_landscape_bottom.png`).
   - **Styled Heading**: `"Enable location"` with `"location"` highlighted in vibrant blue (`#1D61E0`).
   - **Subtitle**: `"Get accurate forecasts, alerts and personalized weather information for your area."`
   - **"Allow Location"**: Dark navy pill button with location pin icon. Launches native Android OS runtime location permission dialog (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`), stores `"ALLOWED"` or `"DENIED"` in `AppPreferences`, marks onboarding completed, and proceeds to `HomeScreen`.
   - **"I'll choose manually"**: Secondary text link inside a semi-transparent white backdrop pill (`#D0FFFFFF`) with bold navy text for 100% legibility over lake water. Stores `"MANUAL"` in `AppPreferences`, marks onboarding completed, and proceeds to `HomeScreen`.

4. **Home Page (`HomeScreen.kt`)**:
   - Displays `"Home Page"` text at the center along with active persistent preferences (Language & Location choice) for verification.

---

## 💾 2. First-Run Persistence & Storage (`AppPreferences.kt`)

Language selections, location choices, and onboarding completion status are saved in `SharedPreferences` (`"mausam_prefs"`), ensuring persistence across app restarts and device reboots.

### First-Install Behavior:
- **First Launch** (`isOnboardingCompleted == false`): User goes through the `Onboarding` ➔ `Language` ➔ `Location` flow.
- **Subsequent Launches** (`isOnboardingCompleted == true`): User bypasses onboarding and launches directly into `HomeScreen`.

```kotlin
object AppPreferences {
    fun getSelectedLanguage(context: Context): String
    fun setSelectedLanguage(context: Context, language: String)

    fun getLocationChoice(context: Context): String
    fun setLocationChoice(context: Context, choice: String)

    fun isOnboardingCompleted(context: Context): Boolean
    fun setOnboardingCompleted(context: Context, completed: Boolean)
}
```

---

## 🔒 3. Location Permission Configuration

To ensure the native Android OS permission popup triggers when clicking `"Allow Location"`, location permissions are declared in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

---

## 📁 4. Architecture & File Structure

```
Mausam/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/mausam/
│   │   │   ├── MainActivity.kt               # Navigation coordinator & permission launcher
│   │   │   ├── ui/
│   │   │   │   ├── OnboardingScreen.kt       # Swipable 3-state landing page with auto-scroll
│   │   │   │   ├── LanguageScreen.kt         # 12-language grid selection UI page matching design
│   │   │   │   ├── LocationScreen.kt         # "Enable location" UI page matching design
│   │   │   │   └── HomeScreen.kt             # Main Home Page
│   │   │   └── utils/
│   │   │       ├── AppPreferences.kt         # SharedPreferences manager for language & location
│   │   │       └── TimeUtils.kt              # LocalTime helper and theme manager
│   │   ├── res/
│   │   │   └── drawable/                    # Graphic assets directory
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── DOCUMENTATION.md                          # Comprehensive app documentation
```

---

## 🖼️ 5. Asset Directory & Image Placement

Image assets are stored in `app/src/main/res/drawable/`:

- **`imd_logo.png`**: Official IMD emblem
- **`bg_day.png`**: Daytime landscape background for onboarding
- **`bg_night.png`**: Nighttime landscape background for onboarding
- **`bg_language_top.png`**: Top sky, sun, clouds, and birds background for Language page
- **`bg_location_top.png`**: Top sky, sun, clouds, and flying birds background for Location page
- **`bg_landscape_bottom.png`**: Bottom landscape (blue mountains, river/lake, pine trees) background for Language & Location pages
- **`img_location_map.png`**: India map graphic with concentric ripple circles and blue location pin
- **`weather_alert_bell.png`**: Bell graphic for Onboarding State 1
- **`mobile.png`**: Smartphone UI graphic for Onboarding State 2
- **`real_time_forecast.png`**, **`temperature_and_feels_like.png`**, **`wind_speed_direction.png`**, **`air_quality.png`**, **`severe_weather_alerts.png`**: Feature chip badges for Onboarding State 2

---

## 🚀 6. Testing & Verification

### Build Command
```bash
./gradlew assembleDebug
```

### Verification Checklist
- [x] Onboarding page automatically scrolls state 0 ➔ 1 ➔ 2 on launch with swipe animation and 100ms delay.
- [x] Skip button is removed from Onboarding page; clicking `"Get Started"` navigates directly to `LanguageScreen`.
- [x] State 2 surrounding feature chips on Onboarding page are sized at `52dp` with padded bounds to prevent edge clipping.
- [x] Language page top sky artwork is bounded to `150dp` height so the sun stays above header text.
- [x] 12 regional language cards fit text cleanly on 1 line without wrapping (e.g. "Malayalam", "Punjabi").
- [x] Location page top sky artwork is bounded to `150dp` height so the sun stays above the map circle.
- [x] `"I'll choose manually"` link on Location page is styled inside a semi-transparent white backdrop pill for 100% legibility over lake water.
- [x] Selecting a language and location choice persists across app restarts in `SharedPreferences`.
