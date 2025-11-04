package com.example.wubbalistdubapp.domain.repository

import com.example.wubbalistdubapp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profileFlow: Flow<Profile>
    suspend fun get(): Profile
    suspend fun save(profile: Profile)
}
