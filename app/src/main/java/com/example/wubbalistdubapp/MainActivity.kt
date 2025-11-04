package com.example.wubbalistdubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.navigation.Routes
import com.example.wubbalistdubapp.ui.CharacterDetailsRoute
import com.example.wubbalistdubapp.ui.CharactersRoute
import com.example.wubbalistdubapp.ui.favorites.FavoritesRoute
import com.example.wubbalistdubapp.ui.filters.FiltersRoute
import androidx.compose.material.icons.filled.Person


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val badgeCache = ServiceLocator.filtersBadgeCache

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val current by navController.currentBackStackEntryAsState()
                            val route = current?.destination?.route
                            fun go(dest: String) {
                                navController.navigate(dest) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            NavigationBarItem(
                                selected = route?.startsWith(Routes.CHARACTERS) == true,
                                onClick = { go(Routes.CHARACTERS) },
                                icon = {
                                    BadgedBox(badge = {
                                        if (badgeCache.showBadge) Badge()
                                    }) { Icon(Icons.Filled.List, null) }
                                },
                                label = { Text("Персонажи") }
                            )
                            NavigationBarItem(
                                selected = route == Routes.EPISODES,
                                onClick = { go(Routes.EPISODES) },
                                icon = { Icon(Icons.Filled.Tv, null) },
                                label = { Text("Эпизоды") }
                            )
                            NavigationBarItem(
                                selected = route == Routes.FAVORITES,
                                onClick = { go(Routes.FAVORITES) },
                                icon = { Icon(Icons.Filled.Favorite, null) },
                                label = { Text("Избранное") }
                            )
                            NavigationBarItem(
                                selected = route == Routes.PROFILE || route == Routes.PROFILE_EDIT,
                                onClick = { go(Routes.PROFILE) },
                                icon = { Icon(Icons.Default.Person, null) },
                                label = { Text("Профиль") }
                            )
                        }
                    }
                ) { inner ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.CHARACTERS,
                        modifier = Modifier.padding(inner)
                    ) {
                        composable(Routes.CHARACTERS) {
                            CharactersRoute(
                                onItemClick = { ch -> navController.navigate(Routes.characterDetails(ch.id)) },
                                onOpenFilters = { navController.navigate(Routes.FILTERS) }
                            )
                        }
                        composable(
                            route = Routes.CHARACTER_DETAILS,
                            arguments = Routes.characterArgs
                        ) {
                            CharacterDetailsRoute(onBack = { navController.popBackStack() })
                        }
                        composable(Routes.FILTERS) {
                            FiltersRoute(onDone = { navController.popBackStack() })
                        }
                        composable(Routes.EPISODES) { PlaceholderScreen("Экран эпизодов (заглушка)") }
                        composable(Routes.FAVORITES) {
                            FavoritesRoute(
                                onItemClick = { ch -> navController.navigate(Routes.characterDetails(ch)) }
                            )
                        }
                        composable(Routes.PROFILE) {
                            com.example.wubbalistdubapp.ui.profile.ProfileRoute(
                                onEdit = { navController.navigate(Routes.PROFILE_EDIT) }
                            )
                        }
                        composable(Routes.PROFILE_EDIT) {
                            com.example.wubbalistdubapp.ui.profile.EditProfileRoute(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Surface(tonalElevation = 1.dp) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.titleLarge)
        }
    }
}
