package com.example.wubbalistdubapp.data.remote.dto

data class CharacterPageResponse(
    val info: PageInfoDto,
    val results: List<CharacterDto>
)

data class PageInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: SimpleRefDto,
    val location: SimpleRefDto,
    val image: String,
    val url: String,
    val created: String
)

data class SimpleRefDto(
    val name: String,
    val url: String
)
