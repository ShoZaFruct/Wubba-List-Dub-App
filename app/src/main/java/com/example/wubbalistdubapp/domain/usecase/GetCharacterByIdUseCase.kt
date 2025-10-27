package com.example.wubbalistdubapp.domain.usecase

import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.repository.CharactersRepository

class GetCharacterByIdUseCase(private val repo: CharactersRepository) {
    suspend operator fun invoke(id: Int): Character = repo.getCharacterById(id)
}
