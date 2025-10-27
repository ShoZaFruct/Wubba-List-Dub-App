package com.example.wubbalistdubapp.data.repository

import com.example.wubbalistdubapp.data.mapper.toDomain
import com.example.wubbalistdubapp.data.remote.api.RickAndMortyApi
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersRepositoryImpl(
    private val api: RickAndMortyApi
) : CharactersRepository {

    override suspend fun getCharacters(
        page: Int?,
        name: String?,
        status: String?,
        gender: String?
    ): List<Character> = withContext(Dispatchers.IO) {
        val resp = api.getCharacters(page = page, name = name, status = status, gender = gender)
        resp.results.orEmpty().map { it.toDomain() }
    }

    override suspend fun getCharacterById(id: Int): Character = withContext(Dispatchers.IO) {
        api.getCharacter(id).toDomain()
    }
}
