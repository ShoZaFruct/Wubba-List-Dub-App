package com.example.wubbalistdubapp.data.remote.api

import com.example.wubbalistdubapp.data.remote.dto.CharacterDto
import com.example.wubbalistdubapp.data.remote.dto.CharacterPageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null
    ): CharacterPageResponse

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}
