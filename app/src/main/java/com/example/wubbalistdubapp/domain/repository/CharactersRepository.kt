package com.example.wubbalistdubapp.domain.repository

import com.example.wubbalistdubapp.domain.model.Character

interface CharactersRepository {
    suspend fun getCharacters(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        gender: String? = null
    ): List<Character>

    suspend fun getCharacterById(id: Int): Character
}
