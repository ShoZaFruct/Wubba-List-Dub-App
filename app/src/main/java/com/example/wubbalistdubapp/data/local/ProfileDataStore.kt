package com.example.wubbalistdubapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wubbalistdubapp.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore("profile_prefs")

class ProfileDataStore(private val context: Context) {

    private val KEY_FULL_NAME: Preferences.Key<String> = stringPreferencesKey("full_name")
    private val KEY_TITLE: Preferences.Key<String> = stringPreferencesKey("title")
    private val KEY_AVATAR_URI: Preferences.Key<String> = stringPreferencesKey("avatar_uri")
    private val KEY_RESUME_URL: Preferences.Key<String> = stringPreferencesKey("resume_url")

    val profileFlow: Flow<Profile> = context.profileDataStore.data.map { p ->
        Profile(
            fullName = p[KEY_FULL_NAME] ?: "",
            title = p[KEY_TITLE] ?: "",
            avatarUri = p[KEY_AVATAR_URI] ?: "",
            resumeUrl = p[KEY_RESUME_URL] ?: ""
        )
    }

    suspend fun get(): Profile = profileFlow.first()

    suspend fun save(profile: Profile) {
        context.profileDataStore.edit { prefs: MutablePreferences ->
            prefs[KEY_FULL_NAME] = profile.fullName
            prefs[KEY_TITLE] = profile.title
            prefs[KEY_AVATAR_URI] = profile.avatarUri
            prefs[KEY_RESUME_URL] = profile.resumeUrl
        }
    }
}
