package com.example.wubbalistdubapp.data.mapper

import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import com.example.wubbalistdubapp.domain.model.Character

fun FavoriteCharacterEntity.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        origin = origin,
        location = location
    )
