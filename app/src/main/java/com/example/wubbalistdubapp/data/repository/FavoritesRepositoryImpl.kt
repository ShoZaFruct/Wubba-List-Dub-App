package com.example.wubbalistdubapp.data.repository

import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import com.example.wubbalistdubapp.data.local.favorites.FavoritesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(
    private val dao: FavoritesDao
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<FavoriteCharacterEntity>> = dao.observeAll()

    override suspend fun isFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        dao.getById(id) != null
    }

    override suspend fun getById(id: Int): FavoriteCharacterEntity? = withContext(Dispatchers.IO) {
        dao.getById(id)
    }

    override suspend fun add(entity: FavoriteCharacterEntity) = withContext(Dispatchers.IO) {
        dao.insert(entity)
    }

    override suspend fun removeById(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }
}
