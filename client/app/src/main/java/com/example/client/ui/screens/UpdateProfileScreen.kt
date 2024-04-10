package com.example.client.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.ui.components.TextFieldOutlined
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.client.R
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.TopBar
import com.example.client.ui.viewmodel.UpdateProfileViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities

private val EDIT_PROFILE_IMG_SIZE = 176.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    name:String,
    avatar:String,
    navController: NavController,
    updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        updateProfileViewModel.onChangeDisplayName(name)
        updateProfileViewModel.onChangeAvatar(avatar)
        updateProfileViewModel.onChangeBottomSheetDisplayName(name)
    }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showUploadImgSection by remember { mutableStateOf(false) }

    var selectedImgUri by remember { mutableStateOf("") }

    val changeAvatar by updateProfileViewModel.changeAvatar.collectAsState()

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->

            if(uri != null){
                selectedImgUri = uri.toString()
                val path = Utilities.getRealPathFromURI(uri, context)
                if (path != null) {
                    updateProfileViewModel.resetChangeAvatarState()
                    updateProfileViewModel.onChangeAvatarPath(path)
                    showUploadImgSection = true
                }
            }
        }

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Expanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    Scaffold(
        topBar = {
            TopBar(
                "Cập Nhật Thông Tin",
                navController
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(it)
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                UpdateProfileImage(modifier = Modifier, updateProfileViewModel.avatar, launcher)

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_small))
                )

                UpdateProfileInfo(
                    modifier = Modifier,
                    subTitle = "Tên Hiển Thị",
                    value = updateProfileViewModel.displayName,
                    onClick = {
                        coroutineScope.launch {
                            updateProfileViewModel.setIsDisplayNameSelected(true)
                            updateProfileViewModel.resetChangeDisplayName()
                            openBottomSheet = true
                            sheetState.show()
                        }
                    }
                )

                if (openBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            openBottomSheet = false
                            updateProfileViewModel.resetChangeDisplayName()
                        },
                        modifier = Modifier.fillMaxSize(),
                        dragHandle = {
                            SheetContent(
                                viewModel = updateProfileViewModel,
                                coroutineScope = coroutineScope,
                                sheetState = sheetState,
                                openBottomSheet = {value ->
                                    openBottomSheet = value
                                }
                            )
                        },
                        sheetState = sheetState,
                        containerColor = MaterialTheme.colorScheme.background
                    ) {}
                }

                if (showUploadImgSection) {
                    UploadImageSection(
                        modifier = Modifier,
                        profileImgUrl = selectedImgUri,
                        onSaveImgClick = {
                            updateProfileViewModel.updateAvatar()
                            if(changeAvatar is ResourceState.Success){
                                showUploadImgSection = false
                            }
                        },
                        onCancelImgClick = {
                            showUploadImgSection = false
                        },
                        changeAvatarState = changeAvatar
                    )
                }
            }
        }
    )
}

@Composable
private fun UpdateProfileImage(
    modifier: Modifier,
    userImage: String,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ProfileImage(
            modifier = modifier,
            userImage = userImage
        )
        Box(
            modifier = modifier.size(EDIT_PROFILE_IMG_SIZE),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .clip(CircleShape),
                onClick = { galleryLauncher.launch("image/*") }
            ) {
                Icon(
                    modifier = modifier.padding(8.dp),
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun UpdateProfileInfo(
    modifier: Modifier,
    subTitle: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = modifier
                .padding(vertical = 32.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.display_name),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = modifier.padding(start = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Icon(
            modifier = modifier.padding(end = 16.dp),
            painter = painterResource(id = R.drawable.edit),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun UploadImageSection(
    modifier: Modifier,
    profileImgUrl: String,
    onSaveImgClick: () -> Unit,
    onCancelImgClick: () -> Unit,
    changeAvatarState: ResourceState<ApiResponse<String>>
) {
    Column(
        modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onBackground)
            .padding(dimensionResource(id = R.dimen.padding_app)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = modifier
                .size(EDIT_PROFILE_IMG_SIZE)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ), shape = CircleShape
                ),
            model = profileImgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement =  Arrangement.spacedBy(16.dp)
            ) {
                ButtonComponent(
                    onClick = onSaveImgClick,
                    value = "Lưu",
                    color = MaterialTheme.colorScheme.primary,
                    loading = changeAvatarState is ResourceState.Loading,
                    enable = changeAvatarState !is ResourceState.Loading
                )
                ButtonNavigate(
                    onClick = onCancelImgClick,
                    value = "Hủy",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun ProfileImage(modifier: Modifier, userImage: String) {
    AsyncImage(
        modifier = modifier
            .size(EDIT_PROFILE_IMG_SIZE)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetContent(
    viewModel: UpdateProfileViewModel,
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    openBottomSheet: (Boolean) -> Unit
) {
    if (viewModel.isDisplayNameSelected) {
        DisplayNameBottomSheet(
            onDisplayNameChange = { viewModel.onChangeBottomSheetDisplayName(it) },
            displayName = viewModel.bottomSheetDisplayName,
            onSaveClick = {
                viewModel.updateDisplayName()
                coroutineScope.launch {
                    sheetState.hide()
                    openBottomSheet(false)
                }
            },
            onCancelClick = {
                coroutineScope.launch {
                    sheetState.hide()
                    openBottomSheet(false)
                }
            }
        )
    }
}

@Composable
private fun DisplayNameBottomSheet(
    onDisplayNameChange: (String) -> Unit,
    displayName: String,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        TextFieldOutlined(
            onChangeValue = { onDisplayNameChange(it) },
            label = "Tên Hiển Thị",
            value = displayName,
            painterResource = painterResource(id = R.drawable.display_name)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancelClick) {
                Text(
                    text = "Hủy",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }
            TextButton(onClick = onSaveClick) {
                Text(
                    text = "Lưu",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 18.sp
                )
            }
        }
    }
}