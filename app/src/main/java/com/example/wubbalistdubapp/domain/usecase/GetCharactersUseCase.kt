package com.example.wubbalistdubapp.domain.usecase

import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCharactersUseCase(private val repo: CharactersRepository) {
    suspend operator fun invoke(page: Int? = null, name: String? = null): List<Character> =
        withContext(Dispatchers.IO) { repo.getCharacters(page, name) }
}
