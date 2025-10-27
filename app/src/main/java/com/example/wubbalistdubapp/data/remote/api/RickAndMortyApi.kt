package com.example.wubbalistdubapp.data.remote.api

import com.example.wubbalistdubapp.data.remote.dto.CharacterDto
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("gender") gender: String? = null
    ): PageResponse<CharacterDto>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}

@JsonClass(generateAdapter = true)
data class PageInfo(
    val count: Int?,
    val pages: Int?,
    val next: String?,
    val prev: String?
)

@JsonClass(generateAdapter = true)
data class PageResponse<T>(
    val info: PageInfo?,
    val results: List<T>?
)
