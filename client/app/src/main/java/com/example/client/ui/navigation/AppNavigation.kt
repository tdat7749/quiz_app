package com.example.client.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.client.ui.screens.HomeScreen
import com.example.client.ui.screens.LoginScreen
import com.example.client.ui.screens.RegisterScreen

@Composable
fun AppNavigationGraph(){

    val navController: NavHostController = rememberNavController()

    val LocalNavController = compositionLocalOf<NavController> {
        error("CompositionLocal LocalNavController not present")
    }

    CompositionLocalProvider(LocalNavController provides navController){
        NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN){

            composable(Routes.LOGIN_SCREEN){
                LoginScreen(LocalNavController.current)
            }

            composable(Routes.REGISTER_SCREEN){
                RegisterScreen(LocalNavController.current)
            }

            composable(Routes.HOME_SCREEN){
                HomeScreen()
            }
        }
    }
}