package com.example.notepadapp.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.viewmodel.NoteViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    Scaffold(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        topBar = { NotePageHeader(navController, noteViewModel) },
        bottomBar = { NotePageFooter(navController, noteViewModel) },
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(contentPadding)
        ) {
            Text(
                "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "wd\n" +
                        "END", fontSize = 30.sp, color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
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