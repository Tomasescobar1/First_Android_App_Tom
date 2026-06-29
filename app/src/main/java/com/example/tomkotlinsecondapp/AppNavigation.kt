package com.example.tomkotlinsecondapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object MainScreen

@Serializable
object DetailsScreen

@Composable fun AppNavigation()
{
     val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreen
    ){

        composable<MainScreen> {
            MainScreen(onNavigateToDetails = {navController.navigate(DetailsScreen)})
        }

        composable<DetailsScreen> {
            DetailsScreen(onNavigateBack = {navController.popBackStack()})
        }

    }
}
