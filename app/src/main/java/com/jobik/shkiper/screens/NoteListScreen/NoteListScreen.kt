package com.jobik.shkiper.screens.NoteListScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.NoteListScreenCalendarContent
import com.jobik.shkiper.screens.NoteListScreen.NoteListScreenContent.NoteListScreenContent
import com.jobik.shkiper.viewmodels.NotesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(navController: NavController) {
    val pagerState = rememberPagerState { 2 }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        state = pagerState,
        pageSpacing = 0.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = PaddingValues(0.dp),
        beyondBoundsPageCount = 0,
        pageSize = PageSize.Fill,
    ) {
        if (it == 0) {
            NoteListScreenContent(navController, hiltViewModel<NotesViewModel>())
        } else {
            NoteListScreenCalendarContent()
        }
    }
}