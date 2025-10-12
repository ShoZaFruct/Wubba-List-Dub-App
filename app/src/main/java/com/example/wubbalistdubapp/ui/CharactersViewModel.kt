package com.example.wubbalistdubapp.ui

import androidx.lifecycle.ViewModel
import com.example.wubbalistdubapp.data.*

class CharactersViewModel(
    private val repo: CharactersRepository = MockCharactersRepository()
) : ViewModel() {
    val characters = repo.getAll()
    fun characterById(id: Int) = repo.getById(id)
}
