package com.example.client.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.client.ui.theme.Shapes
import com.example.client.R
import com.example.client.ui.navigation.Routes


@Composable
fun SmallText(value: String,textAlign: TextAlign,color: Color,navController: NavController?,route: String?){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 32.dp)
            .clickable {
                if(navController != null && route != null){
                    navController.navigate(route)
                }
            },
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Center,
        color = color
    )
}

@Composable
fun NormalText(value: String,textAlign: TextAlign,color: Color){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        textAlign = textAlign,
        color = color
    )
}

@Composable
fun HeadingBoldText(value: String,textAlign: TextAlign,color: Color){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        textAlign = textAlign,
        color = color
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldOutlined(
    value:String,
    onChangeValue: (String) -> Unit,
    label:String,
    painterResource: Painter
){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium),
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        value = value,
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = onChangeValue,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = label
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldOutlined(
    value:String,
    onChangeValue: (String) -> Unit,
    label:String
    ,painterResource: Painter
){
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium),
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = onChangeValue,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = label
            )
        },
        trailingIcon = {
            val icon = if(passwordVisible.value){
                Icons.Filled.Visibility
            }else{
                Icons.Filled.VisibilityOff
            }

            val description = if(passwordVisible.value){
                stringResource(id = R.string.hide_password)
            }else{
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }){
                Icon(imageVector = icon,contentDescription = description)
            }
        },
        visualTransformation = if(passwordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation()
    )
}

@Composable
fun ButtonComponent(
    onClick: () -> Unit,
    value:String,
    color:Color,
    loading: Boolean,
    enable:Boolean,
    loadingColor : Color = MaterialTheme.colorScheme.onPrimary
){
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        colors = ButtonDefaults.buttonColors(color),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button)),
        enabled = enable
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
            contentAlignment = Alignment.Center
        ){
            if(loading){
                CircularProgressIndicator(
                    color = loadingColor
                )
            }else{
                Text(
                    text = value,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}