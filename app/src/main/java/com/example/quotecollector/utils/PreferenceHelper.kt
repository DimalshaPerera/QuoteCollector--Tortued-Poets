package com.example.quotecollector.utils


import android.content.Context

object PreferenceHelper {
    private const val PREF_NAME = "QuoteCollectorPrefs"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_TOKEN = "user_token"
    private const val KEY_USER_ID = "user_id"

    fun saveUserData(context: Context, email: String, token: String, userId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_TOKEN, token)
            putString(KEY_USER_ID, userId)
            apply()
        }
    }

    fun getUserEmail(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_EMAIL, null)
    }

    fun getUserToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_TOKEN, null)
    }

    fun getUserId(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, null)
    }

    fun clearUserData(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}