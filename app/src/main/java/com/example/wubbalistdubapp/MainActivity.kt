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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wubbalistdubapp.navigation.Routes
import com.example.wubbalistdubapp.ui.CharacterDetailsScreen
import com.example.wubbalistdubapp.ui.CharactersScreen
import com.example.wubbalistdubapp.ui.CharactersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val vm: CharactersViewModel = viewModel()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val current by navController.currentBackStackEntryAsState()
                            val route = current?.destination?.route

                            fun go(dest: String) {
                                navController.navigate(dest) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            NavigationBarItem(
                                selected = route?.startsWith(Routes.CHARACTERS) == true,
                                onClick = { go(Routes.CHARACTERS) },
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                label = { Text("Персонажи") }
                            )
                            NavigationBarItem(
                                selected = route == Routes.EPISODES,
                                onClick = { go(Routes.EPISODES) },
                                icon = { Icon(Icons.Default.Tv, contentDescription = null) },
                                label = { Text("Эпизоды") }
                            )
                            NavigationBarItem(
                                selected = route == Routes.FAVORITES,
                                onClick = { go(Routes.FAVORITES) },
                                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                label = { Text("Избранное") }
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
                            CharactersScreen(
                                items = vm.characters,
                                onItemClick = { navController.navigate(Routes.characterDetails(it.id)) }
                            )
                        }
                        composable(Routes.CHARACTER_DETAILS) { entry ->
                            val id = entry.arguments?.getString("id")!!.toInt()
                            val ch = vm.characterById(id)!!
                            CharacterDetailsScreen(character = ch, onBack = { navController.popBackStack() })
                        }
                        composable(Routes.EPISODES) { PlaceholderScreen("Экран эпизодов (заглушка)") }
                        composable(Routes.FAVORITES) { PlaceholderScreen("Экран избранного (заглушка)") }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Surface(tonalElevation = 1.0.dp) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.titleLarge)
        }
    }
}
