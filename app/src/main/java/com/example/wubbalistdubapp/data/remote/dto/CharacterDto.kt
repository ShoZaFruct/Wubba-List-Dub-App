package com.example.wubbalistdubapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val origin: PlaceDto,
    val location: PlaceDto
)

@JsonClass(generateAdapter = true)
data class PlaceDto(
    val name: String,
    @Json(name = "url") val url: String?
)
