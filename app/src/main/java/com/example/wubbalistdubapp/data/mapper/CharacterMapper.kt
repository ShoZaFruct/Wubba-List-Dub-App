package com.example.wubbalistdubapp.data.mapper

import com.example.wubbalistdubapp.data.remote.dto.CharacterDto
import com.example.wubbalistdubapp.domain.model.Character

fun CharacterDto.toDomain() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    origin = origin.name,
    location = location.name
)
