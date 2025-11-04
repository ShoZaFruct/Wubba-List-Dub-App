package com.example.wubbalistdubapp.data.local.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_characters ORDER BY name")
    fun observeAll(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT * FROM favorite_characters WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FavoriteCharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteCharacterEntity)

    @Delete
    suspend fun delete(entity: FavoriteCharacterEntity)

    @Query("DELETE FROM favorite_characters WHERE id = :id")
    suspend fun deleteById(id: Int)
}
