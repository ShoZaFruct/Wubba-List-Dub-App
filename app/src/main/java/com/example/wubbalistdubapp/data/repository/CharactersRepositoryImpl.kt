package com.example.wubbalistdubapp.data.repository

import com.example.wubbalistdubapp.data.mapper.toDomain
import com.example.wubbalistdubapp.data.remote.api.RickAndMortyApi
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository

class CharactersRepositoryImpl(
    private val api: RickAndMortyApi
) : CharactersRepository {

    private val cache = mutableMapOf<Int, Character>()

    override suspend fun getCharacters(page: Int?, name: String?): List<Character> {
        val resp = api.getCharacters(page = page, name = name)
        return resp.results.map { it.toDomain() }
    }

    override suspend fun getCharacterById(id: Int): Character {
        cache[id]?.let { return it }
        val dto = api.getCharacter(id)
        return dto.toDomain().also { cache[id] = it }
    }
}
