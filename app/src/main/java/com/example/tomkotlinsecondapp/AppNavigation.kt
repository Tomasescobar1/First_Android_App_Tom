package com.example.tomkotlinsecondapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
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
import kotlin.time.Duration.Companion.milliseconds

@Serializable
object MainRoute

@Serializable
object MenuRoute

@Serializable
object DetailsRoute

@Composable fun AppNavigation(guitarViewModel: GuitarOrder)
{
     val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = DetailsRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(300)
            )
        }
    )
    {
        composable<DetailsRoute>
        {
            DetailsScreen(onNavigateToMenu = { navigationController.navigate(MenuRoute) })
        }

        composable<MenuRoute>
        {
            MenuScreen(onNavigateToMain = { navigationController.navigate(MainRoute) }, guitarViewModel = guitarViewModel)
        }

        composable<MainRoute>
        {
            MainScreen(guitarViewModel = guitarViewModel)
        }

    }
}
