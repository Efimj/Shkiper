package com.example.notepadapp.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.components.fields.CustomTextField
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.viewmodel.NoteViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NotePage(navController: NavController, noteViewModel: NoteViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    LaunchedEffect(currentRoute) {
        if (currentRoute.substringBefore("/") != UserPage.Note.route.substringBefore("/")) {
            noteViewModel.isTopAppBarHover = false
            noteViewModel.isBottomAppBarHover = false
        }
    }
    var headerValue by remember { mutableStateOf(TextFieldValue()) }
    var bodyValue by remember { mutableStateOf(TextFieldValue()) }

    // Создаем состояние для отслеживания фокуса текстового поля
    val bodyFieldFocusRequester = remember { FocusRequester() }

    Scaffold(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        topBar = { NotePageHeader(navController, noteViewModel) },
        bottomBar = { NotePageFooter(navController, noteViewModel) },
        modifier = Modifier.imePadding().navigationBarsPadding().fillMaxSize(),
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            CustomTextField(
                textFieldValue = headerValue,
                onValueChange = { headerValue = it },
                placeholder = "Header",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onAny = {
                        bodyFieldFocusRequester.requestFocus()
                    }
                ),
                textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(bottom = 6.dp)
            )
            CustomTextField(
                textFieldValue = bodyValue,
                onValueChange = { bodyValue = it },
                placeholder = "Text",
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                    .focusRequester(bodyFieldFocusRequester)
            )
        }
    }

    LaunchedEffect(scrollState.value) {
        if (scrollState.canScrollBackward || scrollState.canScrollForward) {
            noteViewModel.isTopAppBarHover = scrollState.value > 0
            noteViewModel.isBottomAppBarHover = scrollState.value < scrollState.maxValue
        } else {
            noteViewModel.isTopAppBarHover = false
            noteViewModel.isBottomAppBarHover = false
        }
    }
}

@Composable
private fun NotePageHeader(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.isTopAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    TopAppBar(
        elevation = if (noteViewModel.isTopAppBarHover) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        contentColor = CustomAppTheme.colors.textSecondary,
        title = { },
        navigationIcon = {
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
        },
        actions = {
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = "Attach a note",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationAdd,
                    contentDescription = "Add to notification",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Archive,
                    contentDescription = "Add to archive",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NotePageFooter(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.isBottomAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    BottomAppBar(
        elevation = if (noteViewModel.isBottomAppBarHover) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        contentColor = CustomAppTheme.colors.textSecondary,
        cutoutShape = CircleShape,
        modifier = Modifier.fillMaxWidth().height(45.dp),
    ) {
        Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        Text("Last changed: 18:19")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Add to basket",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        }
    }
}