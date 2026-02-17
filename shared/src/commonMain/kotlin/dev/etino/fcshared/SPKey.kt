package dev.etino.fcshared

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

enum class SPKey(val key: Preferences.Key<Boolean>) {

    LOGGED_IN  (booleanPreferencesKey("logged_in")),
    FIRST_TIME ( booleanPreferencesKey("first_time")) ,
    EVENTS_GLOW(booleanPreferencesKey("events_glow")) ,
    TEST_MODE(booleanPreferencesKey("test_mode"))
}