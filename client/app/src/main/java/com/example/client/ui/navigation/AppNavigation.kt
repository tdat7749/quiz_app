package com.example.client.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.client.ui.screens.HomeScreen
import com.example.client.ui.screens.LoginScreen

@Composable
fun AppNavigationGraph(){

    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN){

        composable(Routes.LOGIN_SCREEN){
            LoginScreen()
        }

        composable(Routes.HOME_SCREEN){
            HomeScreen()
        }


    }
}