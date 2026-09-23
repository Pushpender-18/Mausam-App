package com.example.mausam.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences wrapper to persist app-wide user settings (Selected Language, Location Choice, Onboarding Status).
 */
object AppPreferences {
    private const val PREF_NAME = "mausam_prefs"
    private const val KEY_SELECTED_LANGUAGE = "selected_language"
    private const val KEY_LOCATION_CHOICE = "location_choice"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getSelectedLanguage(context: Context): String {
        return getPrefs(context).getString(KEY_SELECTED_LANGUAGE, "English") ?: "English"
    }

    fun setSelectedLanguage(context: Context, language: String) {
        getPrefs(context).edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }

    fun getLocationChoice(context: Context): String {
        return getPrefs(context).getString(KEY_LOCATION_CHOICE, "NONE") ?: "NONE"
    }

    fun setLocationChoice(context: Context, choice: String) {
        getPrefs(context).edit().putString(KEY_LOCATION_CHOICE, choice).apply()
    }

    fun isOnboardingCompleted(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    private const val KEY_PERSONALIZED_CATEGORIES = "personalized_categories"

    fun getPersonalizedCategories(context: Context): Set<String> {
        return getPrefs(context).getStringSet(KEY_PERSONALIZED_CATEGORIES, emptySet()) ?: emptySet()
    }

    fun setPersonalizedCategories(context: Context, categories: Set<String>) {
        getPrefs(context).edit().putStringSet(KEY_PERSONALIZED_CATEGORIES, categories).apply()
    }
}
