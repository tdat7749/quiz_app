package com.example.client.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.client.R
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.HeaderApp
import com.example.client.ui.components.PasswordFieldOutlined
import com.example.client.ui.viewmodel.ChangePasswordViewModel
import com.example.client.ui.viewmodel.LoginViewModel
import com.example.client.utils.ResourceState

@Composable
fun ChangePasswordScreen (
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
){
   Surface (
       modifier = Modifier
           .fillMaxSize()
           .padding(dimensionResource(id = R.dimen.padding_app))
           .verticalScroll(rememberScrollState()),
       color = Color.White
   ) {

       Column (
           modifier = Modifier
               .fillMaxSize()
       ) {
           HeaderApp(
               painterResource(id = R.drawable.padlock),
               stringResource(id = R.string.app_name),
               stringResource(id = R.string.change_password)
           )
           PasswordFieldOutlined(
               value = changePasswordViewModel.oldPassword,
               onChangeValue = {
                   changePasswordViewModel.onChangeOldPassword(it)
               },
               label = stringResource(id = R.string.old_password),
               painterResource = painterResource(id = R.drawable.password)
           )
           Spacer(
               modifier = Modifier
                   .height(dimensionResource(id = R.dimen.space_app_normal))
           )
           PasswordFieldOutlined(
               value = changePasswordViewModel.newPassword,
               onChangeValue = {
                   changePasswordViewModel.onChangeNewPassword(it)
               },
               label = stringResource(id = R.string.new_password),
               painterResource = painterResource(id = R.drawable.password)
           )
           Spacer(
               modifier = Modifier
                   .height(dimensionResource(id = R.dimen.space_app_normal))
           )
           PasswordFieldOutlined(
               value = changePasswordViewModel.confirmPassword,
               onChangeValue = {
                   changePasswordViewModel.onChangeConfirmPassword(it)
               },
               label = stringResource(id = R.string.confirm_password),
               painterResource = painterResource(id = R.drawable.password)
           )
           Spacer(
               modifier = Modifier
                   .height(dimensionResource(id = R.dimen.space_app_normal))
           )

           ButtonComponent(
               onClick = {

               },
               stringResource(id = R.string.change_password),
               MaterialTheme.colorScheme.primary,
               false,
               false
           )
       }
   }
}

@Preview
@Composable
fun ChangePasswordScreenPreview(){
    ChangePasswordScreen()
}