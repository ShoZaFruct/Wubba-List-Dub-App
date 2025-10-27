package com.example.wubbalistdubapp.data.repository

import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoriteCharacterEntity>>
    suspend fun isFavorite(id: Int): Boolean
    suspend fun getById(id: Int): FavoriteCharacterEntity?
    suspend fun add(entity: FavoriteCharacterEntity)
    suspend fun removeById(id: Int)
}
