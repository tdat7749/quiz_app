package com.example.client.ui.navigation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.client.ui.screens.*

@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNavigationGraph(){

    val navController: NavHostController = rememberNavController()

        NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN){

            composable(Routes.LOGIN_SCREEN){
                LoginScreen(navController)
            }

            composable(Routes.REGISTER_SCREEN){
                RegisterScreen(navController)
            }

            composable(Routes.HOME_SCREEN){
                HomeScreen(navController)
            }

            composable(Routes.VERIFY_SCREEN){
                VerifyAccountScreen(navController)
            }

            composable(
                route = "${Routes.TOPIC_SCREEN}/{topicId}/{title}/{thumbnail}",
                arguments = listOf(
                    navArgument("topicId") { type = NavType.IntType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("thumbnail") { type = NavType.StringType }
                )
            ){backStackEntry ->
                val topicId = backStackEntry.arguments?.getInt("topicId")
                val title = backStackEntry.arguments?.getString("title")
                val thumbnail = backStackEntry.arguments?.getString("thumbnail")
                TopicScreen(topicId!!, Uri.decode(title!!),Uri.decode(thumbnail!!))
            }
        }
}