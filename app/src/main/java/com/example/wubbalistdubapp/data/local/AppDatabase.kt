package com.example.wubbalistdubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import com.example.wubbalistdubapp.data.local.favorites.FavoritesDao

@Database(
    version = 1,
    entities = [FavoriteCharacterEntity::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
