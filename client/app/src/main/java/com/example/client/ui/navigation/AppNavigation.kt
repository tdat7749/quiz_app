package com.example.client.ui.navigation

import EditRoomScreen
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
import com.example.client.ui.viewmodel.EditQuizViewModel
import com.example.client.ui.viewmodel.EditRoomViewModel
import com.example.client.ui.viewmodel.PlayQuizViewModel
import java.lang.Integer.parseInt

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNavigationGraph(){

    val navController: NavHostController = rememberNavController()
    val createQuizViewModel:CreateQuizViewModel = hiltViewModel()
    val playQuizViewModel:PlayQuizViewModel = hiltViewModel()
    val editQuizViewModel:EditQuizViewModel = hiltViewModel()

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

            composable(Routes.CHANGE_PASSWORD_SCREEN){
                ChangePasswordScreen(navController)
            }

            composable(Routes.FIND_ROOM_SCREEN){
                FindRoomScreen(navController)
            }


            composable(Routes.PROFILE_SCREEN){
                ProfileScreen(navController)
            }

            composable(Routes.CREATE_QUIZ_SCREEN){
                CreateQuizScreen(navController,createQuizViewModel)
            }

            composable(Routes.MY_QUIZZES_SCREEN){
                MyQuizzesScreen(navController)
            }

            composable(Routes.MY_ROOMS_SCREEN){
                MyRoomsScreen(navController)
            }

            composable(Routes.COLLECTION_SCREEN){
                CollectionScreen(navController)
            }

            composable(
                route = "${Routes.ROOM_DETAIL_SCREEN}/{id}",
                arguments = listOf(
                    navArgument("id") {type = NavType.IntType},
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                RoomDetailScreen(id!!,navController)
            }

            composable(
                route = "${Routes.EDIT_QUIZ_SCREEN}/{id}",
                arguments = listOf(
                    navArgument("id") {type = NavType.IntType},
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                EditQuizScreen(id!!,navController,editQuizViewModel)
            }

            composable(
                route = "${Routes.EDIT_QUESTION_SCREEN}/{quizId}/{index}",
                arguments = listOf(
                    navArgument("quizId") {type = NavType.IntType},
                    navArgument("index") {type = NavType.IntType}
                )
            ){navStackEntry ->
                val quizId = navStackEntry.arguments?.getInt("quizId")
                val index = navStackEntry.arguments?.getInt("index")
                EditQuestionScreen(quizId!!,index!!,navController,editQuizViewModel)
            }

            composable(
                route = "${Routes.QUIZ_DETAIL_SCREEN}/{id}",
                arguments = listOf(
                    navArgument("id") {type = NavType.IntType},
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                QuizDetailScreen(id!!,navController)
            }

            composable(
                route = "${Routes.EDIT_ROOM_SCREEN}/{id}",
                arguments = listOf(
                    navArgument("id") {type = NavType.IntType},
                )
            ){navStackEntry ->
                val id = navStackEntry.arguments?.getInt("id")
                EditRoomScreen(id!!,navController)
            }

            composable(
                route = "${Routes.UPDATE_PROFILE_SCREEN}/{name}/{avatar}",
                arguments = listOf(
                    navArgument("name") {type = NavType.StringType},
                    navArgument("avatar") {type = NavType.StringType}
                )
            ){navStackEntry ->
                val name = navStackEntry.arguments?.getString("name")
                var avatar = navStackEntry.arguments?.getString("avatar")
                if(Uri.decode(avatar)!!.equals("null")){
                    avatar = Uri.encode("")
                }
                UpdateProfileScreen(Uri.decode(name)!!,Uri.decode(avatar)!!,navController)
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

            composable(
                route = "${Routes.QUIZ_RESULT_SCREEN}/{id}/{roomId}",
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
                ResultQuizScreen(id!!,roomId,navController,playQuizViewModel)
            }

            composable(
                route = "${Routes.QUIZ_RANK_SCREEN}/{id}/{roomId}",
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
                RankScreen(id!!,roomId,navController)
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
                route = "${Routes.CREATE_ROOM_SCREEN}/{quizId}/{title}/{thumbnail}",
                arguments = listOf(
                    navArgument("quizId") { type = NavType.IntType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("thumbnail") { type = NavType.StringType }
                )
            ){backStackEntry ->
                val quizId = backStackEntry.arguments?.getInt("quizId")
                val title = backStackEntry.arguments?.getString("title")
                val thumbnail = backStackEntry.arguments?.getString("thumbnail")
                CreateRoomScreen(quizId!!, Uri.decode(title!!),Uri.decode(thumbnail!!),navController)
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