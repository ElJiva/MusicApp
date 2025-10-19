package com.ivan.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ivan.musicapp.ui.theme.HomeScreenRoute
import com.ivan.musicapp.ui.theme.AlbumDetailScreenRoute
import com.ivan.musicapp.screens.DetailScreen
import com.ivan.musicapp.screens.HomeScreen
import com.ivan.musicapp.ui.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeScreenRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<HomeScreenRoute> {
                            HomeScreen(
                                onAlbumClick = { id ->
                                    navController.navigate(AlbumDetailScreenRoute(id))
                                }
                            )
                        }
                        composable<AlbumDetailScreenRoute> { backStackEntry ->
                            val route = backStackEntry.toRoute<AlbumDetailScreenRoute>()
                            DetailScreen(id = route.id)
                        }
                    }
                }
            }
        }
    }
}
