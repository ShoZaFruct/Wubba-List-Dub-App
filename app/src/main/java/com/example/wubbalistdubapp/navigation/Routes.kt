package com.example.wubbalistdubapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object Routes {
    const val CHARACTERS = "characters"
    const val CHARACTER_DETAILS = "character/{id}"
    fun characterDetails(id: Int) = "character/$id"
    val characterArgs = listOf(navArgument("id") { type = NavType.IntType })

    const val EPISODES = "episodes"
    const val FAVORITES = "favorites"

    const val FILTERS = "filters"

    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
}
