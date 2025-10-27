package com.example.wubbalistdubapp.di

import android.content.Context
import androidx.room.Room
import com.example.wubbalistdubapp.data.local.AppDatabase
import com.example.wubbalistdubapp.data.local.FiltersDataStore
import com.example.wubbalistdubapp.data.remote.api.NetworkModule
import com.example.wubbalistdubapp.data.repository.CharactersRepositoryImpl
import com.example.wubbalistdubapp.data.repository.FavoritesRepository
import com.example.wubbalistdubapp.data.repository.FavoritesRepositoryImpl
import com.example.wubbalistdubapp.domain.repository.CharactersRepository
import com.example.wubbalistdubapp.domain.usecase.GetCharacterByIdUseCase
import com.example.wubbalistdubapp.domain.usecase.GetCharactersUseCase

object ServiceLocator {

    private lateinit var appContext: Context
    fun init(context: Context) { appContext = context.applicationContext }

    // API + репо персонажей
    private val api by lazy { NetworkModule.api }
    val charactersRepository: CharactersRepository by lazy { CharactersRepositoryImpl(api) }
    val getCharacters by lazy { GetCharactersUseCase(charactersRepository) }
    val getCharacterById by lazy { GetCharacterByIdUseCase(charactersRepository) }

    // DataStore
    val filtersDataStore by lazy { FiltersDataStore(appContext) }

    // Room
    private val db by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "wubba.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    private val favoritesDao by lazy { db.favoritesDao() }
    val favoritesRepository: FavoritesRepository by lazy { FavoritesRepositoryImpl(favoritesDao) }

    // Badge cache
    val filtersBadgeCache by lazy { FiltersBadgeCache() }
}
