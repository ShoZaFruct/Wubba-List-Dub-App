package com.example.wubbalistdubapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import com.example.wubbalistdubapp.di.ServiceLocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel : ViewModel() {
    private val repo = ServiceLocator.favoritesRepository
    val favorites: StateFlow<List<FavoriteCharacterEntity>> =
        repo.observeFavorites().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
