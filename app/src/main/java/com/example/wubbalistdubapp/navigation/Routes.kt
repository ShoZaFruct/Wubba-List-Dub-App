package com.example.wubbalistdubapp.navigation

object Routes {
    const val CHARACTERS = "characters"
    const val CHARACTER_DETAILS = "character/{id}"
    fun characterDetails(id: Int) = "character/$id"

    const val EPISODES = "episodes"
    const val FAVORITES = "favorites"
}


