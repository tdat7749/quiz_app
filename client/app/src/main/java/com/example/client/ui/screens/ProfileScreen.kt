package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.model.UserDetail
import com.example.client.ui.components.BottomBar
import com.example.client.ui.components.Loading
import com.example.client.ui.viewmodel.ProfileViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.R
import com.example.client.ui.navigation.Routes


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController:NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
){

    val me by profileViewModel.me.collectAsState()

    LaunchedEffect(Unit){
        profileViewModel.getMeDetail()
    }

    DisposableEffect(Unit){
        onDispose {
            profileViewModel.resetState()
        }
    }

    Scaffold(
        bottomBar =  {
            BottomBar(navController)
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(it)
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                when(me){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val user = (me as ResourceState.Success<ApiResponse<UserDetail>>).value.data
                        ProfileSection(
                            displayName = user.displayName,
                            userImage = user.avatar,
                            onEditProfileClick = {
                                navController.navigate("${Routes.UPDATE_PROFILE_SCREEN}/${Uri.encode(user.displayName)}/${Uri.encode(if(user.avatar == "") "null" else user.avatar )}")
                            },
                            onPreferenceClick = {
                                navController.navigate(it)
                            }
                        )
                    }
                    else -> {

                    }
                }
            }
        }
    )
}



@Composable
private fun ProfileSection(
    displayName: String,
    userImage: String,
    onEditProfileClick: () -> Unit,
    onPreferenceClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ProfileImage(
                modifier = Modifier,
                userImage = userImage
            )
            UserRealNameAndUserName(
                displayName = displayName,
            )
            EditProfileButton(onEditProfileClick = onEditProfileClick)
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_large))
        )
        Preferences(onPreferenceClick = onPreferenceClick)
    }
}

@Composable
private fun UserRealNameAndUserName(
    displayName: String
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "@$displayName",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun EditProfileButton(onEditProfileClick: () -> Unit) {
    TextButton(
        modifier = Modifier.padding(top = 16.dp),
        onClick = onEditProfileClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.textButtonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "Cập Nhật",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun Preferences(onPreferenceClick: (String) -> Unit) {
    preferenceList.forEach {
        Preference(
            iconId = it.iconId,
            text = it.text,
            onPreferenceClick = { onPreferenceClick(it.routes) },
            action = it.action,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
    }
}

@Composable
private fun Preference(
    modifier: Modifier = Modifier,
    iconId: Int,
    text: String,
    onPreferenceClick: () -> Unit,
    action: @Composable RowScope.() -> Unit,
    backgroundColor: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onPreferenceClick)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Icon(painter = painterResource(id = iconId), contentDescription = null)
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        action()
    }
}


@Composable
private fun ProfileImage(modifier: Modifier, userImage: String) {
    AsyncImage(
        modifier = modifier
            .size(108.dp)
            .clip(CircleShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ), shape = CircleShape
            ),
        model = userImage,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

data class PreferenceModel(
    val routes: String,
    val iconId: Int,
    val text: String,
    val onPreferenceClick: () -> Unit,
    val action: @Composable RowScope.() -> Unit
)

private val preferenceList = listOf(
    PreferenceModel(
        routes = Routes.MY_ROOMS_SCREEN,
        iconId = R.drawable.password,
        text = "Danh Sách Phòng",
        onPreferenceClick = {}
    ) {
        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    },
    PreferenceModel(
        routes = Routes.MY_QUIZZES_SCREEN,
        iconId = R.drawable.password,
        text = "Danh Sách Quiz",
        onPreferenceClick = {}
    ) {
        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    },
    PreferenceModel(
        routes = Routes.COLLECTION_SCREEN,
        iconId = R.drawable.password,
        text = "Bộ Sưu Tập",
        onPreferenceClick = { }
    ) {
        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    },
    PreferenceModel(
        routes = Routes.CREATE_QUIZ_SCREEN,
        iconId = R.drawable.password,
        text = "Tạo Quiz",
        onPreferenceClick = {}
    ) {
        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    },
    PreferenceModel(
        routes = Routes.CHANGE_PASSWORD_SCREEN,
        iconId = R.drawable.password,
        text = "Đổi Mật khẩu",
        onPreferenceClick = { }
    ) {
        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    },
)