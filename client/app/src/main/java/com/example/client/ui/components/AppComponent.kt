package com.example.client.ui.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.ui.theme.Shapes
import com.example.client.R
import com.example.client.model.QuestionType
import com.example.client.model.Quiz
import com.example.client.model.Topic
import com.example.client.ui.navigation.Routes
import com.example.client.ui.screens.UserInfo
import com.example.client.utils.AppConstants


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
fun NormalText(value: String,textAlign: TextAlign,color: Color,modifier: Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
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
fun HeadingBoldText(value: String, textAlign: TextAlign, color: Color, fontSize: TextUnit, modifier:Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
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
    painterResource: Painter,
    enable: Boolean = true
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
        },
        enabled = enable
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberFieldOutlined(
    value:Int,
    onChangeValue: (String) -> Unit,
    label:String,
    painterResource: Painter,
    enable: Boolean = true
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
        value = value.toString(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = onChangeValue,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = label
            )
        },
        enabled = enable
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    value:String,
    onChangeValue: (String) -> Unit,
    label:String,
    painterResource: Painter,
    onSearch: () -> Unit
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        onValueChange = onChangeValue,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = label
            )
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        )
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

@Composable
fun ButtonNavigate(
    onClick: () -> Unit,
    value:String,
    color:Color,
){
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        colors = ButtonDefaults.buttonColors(color),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button)),
    ){
        Text(
            text = value,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Loading(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(100.dp)
        )
    }
}

@Composable
fun HeaderApp(painterResource: Painter,headingText:String,normalText:String){
    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_extraLarge))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center

    ){
        Image(
            painter = painterResource,
            contentDescription = stringResource(id = R.string.logo_description),
            modifier = Modifier
                .size(200.dp)
        )
    }
    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_small))
    )
    HeadingBoldText(
        headingText,
        TextAlign.Center,
        MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        fontSize = 30.sp
    )
    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_small))
    )
    NormalText(
        normalText,
        TextAlign.Center,
        MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    )
    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_normal))
    )
}

@Composable
fun QuizCard(quiz:Quiz){
    Box(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.quiz_card_height))
                .shadow(4.dp,shape = RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = "https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.image_height))
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    HeadingBoldText(
                        quiz.title,
                        TextAlign.Start,
                        MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 24.sp
                    )

                    UserInfo(quiz.user)
                }
            }
        }
    }
}

@Composable
fun ScreenHeader(title:String,thumbnail:String? = null,painterResource: Painter? = null,navController: NavController,showBackIcon:Boolean = false){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.search_header)),
        contentAlignment = Alignment.BottomCenter
    ){
        if(thumbnail != null){
            AsyncImage(
                model = thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.search_header))
            )
        }
        if(painterResource != null){
            Image(
                painter = painterResource,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.search_header))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            if (showBackIcon) {
                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa icon và tiêu đề
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (title:String,navController: NavController){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}

@Composable
fun EmailDisplay(email:String){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = email,
            style = TextStyle(fontSize = 18.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun BottomBar(navController:NavController){

    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(
            onClick = {
                selected.value = Icons.Default.Home
                navController.navigate(Routes.HOME_SCREEN) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
            )

        }

        Box(modifier = Modifier
            .weight(1f)
            .padding(16.dp),
            contentAlignment = Alignment.Center){
            FloatingActionButton(
                onClick = { showBottomSheet = true },) {
                Icon(Icons.Default.Search,contentDescription = null,tint = MaterialTheme.colorScheme.primary)
            }
        }


        IconButton(
            onClick = {
                selected.value = Icons.Default.Person
                navController.navigate(Routes.CREATE_QUIZ_SCREEN) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.Person) Color.White else Color.DarkGray
            )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTopicMenu(
    options:List<Topic>,
    selectedOption:Topic?,
    onChangeValue: (Topic) -> Unit
    ){

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption?.title ?: "",
            onValueChange = { },
            label = { Text("Chủ Đề") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth().clip(Shapes.medium),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach {option ->
                DropdownMenuItem(
                    text = { Text(option.title) },
                    onClick = {
                        onChangeValue(option)
                        expanded = false
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownQuestionType(
    options:List<QuestionType>,
    selectedOption:QuestionType?,
    onChangeValue: (QuestionType) -> Unit
){

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption?.title ?: "",
            onValueChange = { },
            label = { Text("Loại Câu Hỏi") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth().clip(Shapes.medium),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach {option ->
                DropdownMenuItem(
                    text = { Text(option.title) },
                    onClick = {
                        onChangeValue(option)
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun SwitchLabel(
    label:String,
    value:Boolean,
    onChangeValue: (Boolean) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = value,
            onCheckedChange = { newSwitchState ->
                onChangeValue(newSwitchState)
            }
        )
    }
}

@Composable
fun ImageCard(imageUri:Uri?){
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.quiz_card_height))
    ) {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}