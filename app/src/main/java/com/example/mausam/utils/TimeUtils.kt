package com.example.mausam.utils

import java.time.LocalTime

/**
 * Utility for handling system time based UI page selection (Day vs Night).
 */
object TimeUtils {
    private val DAY_START_TIME: LocalTime = LocalTime.of(6, 0)   // 06:00 AM
    private val NIGHT_START_TIME: LocalTime = LocalTime.of(18, 0) // 06:00 PM

    /**
     * Checks whether the given [time] (defaults to current system time) is during daytime.
     * Daytime is defined as between 06:00 AM (inclusive) and 06:00 PM (exclusive).
     */
    fun isDaytime(time: LocalTime = LocalTime.now()): Boolean {
        return !time.isBefore(DAY_START_TIME) && time.isBefore(NIGHT_START_TIME)
    }
}

/**
 * Mode override options for previewing or forcing Day/Night pages.
 */
enum class PageTimeMode {
    AUTO,
    FORCE_DAY,
    FORCE_NIGHT
}
