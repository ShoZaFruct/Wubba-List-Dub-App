package com.example.wubbalistdubapp.domain.usecase

import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCharacterByIdUseCase(private val repo: CharactersRepository) {
    suspend operator fun invoke(id: Int): Character =
        withContext(Dispatchers.IO) { repo.getCharacterById(id) }
}
