package com.example.wubbalistdubapp.domain.usecase

import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository

class GetCharactersUseCase(private val repo: CharactersRepository) {
    suspend operator fun invoke(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        gender: String? = null
    ): List<Character> = repo.getCharacters(page, name, status, gender)
}
