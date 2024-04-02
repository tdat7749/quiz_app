package com.example.client.ui.navigation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.client.ui.screens.*
import com.example.client.ui.viewmodel.CreateQuizViewModel
import com.example.client.ui.viewmodel.PlayQuizViewModel
import java.lang.Integer.parseInt

@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNavigationGraph(){

    val navController: NavHostController = rememberNavController()
    val createQuizViewModel:CreateQuizViewModel = hiltViewModel()
    val playQuizViewModel:PlayQuizViewModel = hiltViewModel()

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

            composable(Routes.SEND_EMAIL_FORGOT_SCREEN){
                SendEmailForgotScreen(navController)
            }

            composable(Routes.SEND_EMAIL_VERIFY_SCREEN){
                SendEmailVerifyScreen(navController)
            }

            composable(Routes.CREATE_QUIZ_SCREEN){
                CreateQuizScreen(navController,createQuizViewModel)
            }

            composable(
                route = "${Routes.QUIZ_LADING_SCREEN}/{id}",
                arguments = listOf(
                    navArgument("id"){ type = NavType.IntType}
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                QuizLanding(id!!,navController = navController)
            }

            composable(
                route = "${Routes.PLAY_QUIZ_SCREEN}/{id}/{roomId}",
                arguments = listOf(
                    navArgument("id"){ type = NavType.IntType},
                    navArgument("roomId"){ type = NavType.StringType}
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                val roomIdString = navStackEntry.arguments?.getString("roomId")
                var roomId: Int? = null;
                if(roomIdString.equals("null")){
                    roomId = null
                }else{
                    roomId = roomIdString?.let { parseInt(it) }
                }

                PlayQuizScreen(id!!,roomId = roomId,navController = navController, playQuizViewModel =playQuizViewModel)
            }

            composable(Routes.QUIZ_RESULT_SCREEN){
                ResultQuizScreen(navController,playQuizViewModel)
            }

            composable(
                route = "${Routes.QUESTION_SCREEN}/{index}",
                arguments = listOf(
                    navArgument("index"){ type = NavType.IntType}
                )
                ){navStackEntry ->
                val index = navStackEntry.arguments?.getInt("index")
                QuestionScreen(index!!,navController,createQuizViewModel)
            }

            composable(
                route = "${Routes.FORGOT_PASSWORD_SCREEN}/{email}",
                arguments = listOf(
                    navArgument("email"){ type = NavType.StringType}
                )
            ){navStackEntry ->
                val email = navStackEntry.arguments?.getString("email")
                ForgotPasswordScreen(email!!,navController)
            }

            composable(
                route = "${Routes.VERIFY_SCREEN}/{email}",
                arguments = listOf(
                    navArgument("email"){ type = NavType.StringType}
                )
            ){navStackEntry ->
                val email = navStackEntry.arguments?.getString("email")
                VerifyAccountScreen(email!!,navController)
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
                TopicScreen(topicId!!, Uri.decode(title!!),Uri.decode(thumbnail!!),navController)
            }
        }
}