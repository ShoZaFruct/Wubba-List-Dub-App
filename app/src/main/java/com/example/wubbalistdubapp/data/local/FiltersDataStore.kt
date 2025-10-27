package com.example.wubbalistdubapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wubbalistdubapp.domain.model.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.filtersDataStore by preferencesDataStore(name = "filters_prefs")

class FiltersDataStore(private val context: Context) {

    private val KEY_NAME: Preferences.Key<String> = stringPreferencesKey("name")
    private val KEY_STATUS: Preferences.Key<String> = stringPreferencesKey("status")
    private val KEY_GENDER: Preferences.Key<String> = stringPreferencesKey("gender")

    val filtersFlow: Flow<Filters> = context.filtersDataStore.data.map { pref ->
        Filters(
            name = pref[KEY_NAME] ?: "",
            status = pref[KEY_STATUS] ?: "any",
            gender = pref[KEY_GENDER] ?: "any"
        )
    }

    suspend fun save(filters: Filters) {
        context.filtersDataStore.edit { prefs: MutablePreferences ->
            prefs[KEY_NAME] = filters.name
            prefs[KEY_STATUS] = filters.status
            prefs[KEY_GENDER] = filters.gender
        }
    }
}
