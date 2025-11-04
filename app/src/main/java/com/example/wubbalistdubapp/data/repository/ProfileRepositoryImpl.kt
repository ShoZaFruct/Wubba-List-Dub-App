package com.example.wubbalistdubapp.data.repository

import com.example.wubbalistdubapp.data.local.ProfileDataStore
import com.example.wubbalistdubapp.domain.model.Profile
import com.example.wubbalistdubapp.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val store: ProfileDataStore
) : ProfileRepository {

    override val profileFlow: Flow<Profile> = store.profileFlow

    override suspend fun get(): Profile = withContext(Dispatchers.IO) { store.get() }

    override suspend fun save(profile: Profile) = withContext(Dispatchers.IO) {
        store.save(profile)
    }
}
