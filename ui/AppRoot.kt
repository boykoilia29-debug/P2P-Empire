package com.grayscheme.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grayscheme.data.GameRepository
import com.grayscheme.ui.screens.PhoneScreen
import com.grayscheme.ui.screens.StartMenuScreen
import com.grayscheme.ui.screens.apps.*

private val slideUp: EnterTransition =
    slideInVertically(animationSpec = tween(300), initialOffsetY = { it }) + fadeIn(tween(300))
private val slideDown: ExitTransition =
    slideOutVertically(animationSpec = tween(250), targetOffsetY = { it }) + fadeOut(tween(250))

@Composable
fun AppRoot(repo: GameRepository) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "start") {
        composable("start") { StartMenuScreen(repo, nav) }
        composable("phone") { PhoneScreen(repo, nav) }
        composable("telegram", enterTransition = { slideUp }, exitTransition = { slideDown }) {
            TelegramScreen(repo, nav)
        }
        composable("bibit", enterTransition = { slideUp }, exitTransition = { slideDown }) {
            BibitScreen(repo, nav)
        }
        composable("keeper", enterTransition = { slideUp }, exitTransition = { slideDown }) {
            KeeperScreen(repo, nav)
        }
        composable("avito", enterTransition = { slideUp }, exitTransition = { slideDown }) {
            AvitoScreen(repo, nav)
        }
        composable("bank", enterTransition = { slideUp }, exitTransition = { slideDown }) {
            BankScreen(repo, nav)
        }
    }
}