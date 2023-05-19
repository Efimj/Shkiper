package com.example.notepadapp.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    var textValue by remember { mutableStateOf(TextFieldValue()) }
    var textBoxHeight by remember { mutableStateOf(0) }

    LaunchedEffect(scrollState.maxValue, textValue.text) {
        // Value indicating the scroll position from the start
        val scrollPositionToStart = scrollState.value
        // Value indicating the scroll position to the end
        val scrollPositionToEnd = textBoxHeight + scrollState.value
        if (textValue.selection.start > scrollPositionToStart && scrollPositionToEnd > textValue.selection.start) {
            return@LaunchedEffect
        }
        if (textValue.selection.start < scrollPositionToStart) {
            scrollState.animateScrollTo(textValue.selection.start)
            return@LaunchedEffect
        }
        if (scrollPositionToEnd < textValue.selection.start) {
            scrollState.animateScrollTo(textValue.selection.start - textBoxHeight + 50)
            return@LaunchedEffect
        }
    }

    LaunchedEffect(Unit) {
        textValue = textValue.copy(
            text = "Gaunter O'Dim is one of the most interesting characters ever written! I'd love to see more of him.1 тыс.Ответить26 ответовssss3 года назад0:00 - Hearts Of Stone2:55 - Go Back Whence You Came4:42 - You're... Immortal?7:38 - Evil's Soft First Touches10:28 - Dead Man's Party11:18 - Mystery Man14:01 - Breaking In15:44 - Whatsoever A Man Soweth...18:07 - The House Of The Borsodis19:04 - The Temple Of Lilvani 21:27 - A Gifted Man Brings Gifts Galorefor phone users :)368Ответить10 ответовspaceman256spaceman2567 лет назадHearts of Stone really caught my attention, and for a very specific reason.When i was younger, i would occasionally have nightmares, as anyone would. Nightmares where i would be chased by monsters or be attempting to escape some sort of natural disaster. However, for me these nightmares always ended in the same way. As soon as escape became impossible and death became inevitable, i would always clap my hands twice, which would cause time to freeze and allow me to escape. When Master Mirror did exactly the same thing in the Alchemy Inn, it was the first time a fictional character had ever caused real, genuine fear in me. If any other character had done that, i still would've been at least slightly dumbfounded. But the fact that Mirror's character is so mysterious, so enigmatic, so intangible, it felt like he had pulled that scene out of the depths of my memory specifically for me.And i'll never forget that feeling.2,6 тыс.Ответить52 ответаChris MniChris Mni6 лет назадWitcher 3 including all DLC's are definitely the best in gaming I've experienced in my life. Superior graphics, excellent scripting. perfect sound and more immersive than everything I've ever seen.247Ответить6 ответовAciebelAciebel7 лет назадYou know an OST is good when it makes you launch the game again after a long time.427ОтветитьMoisés FreireMoisés Freire7 лет назадMarcin Przybyłowicz and Percival just deserves a fucking grammy for this work. Genius.289Ответить1 ответCyberFrankCyberFrank7 лет назадMy god... as if the main soundtrack wasn't already amazing enough...519Ответить1 ответLynchpin00Lynchpin007 лет назадThe Witcher 3's soundtrack is really turning about to be one of the greatest in video game history. Each new piece they add just keeps making it better. Source: A 39 year old guy who's been playing video games obsessively since age 6.635Ответить9 ответовLange033Lange0336 лет назадthis soundtrack gives me the shivers knowing the story behind iti will never forget my first playtrough of this dlc.167Ответить3 ответаTurski BlazejTurski Blazej6 лет назадLiczko ma gładkie i mowę kwiecistą.Czarci ten pomiot z naturą nieczystą.Życzenia Twe spełni, zawsze z ochotą.Da Ci brylanty i srebro i złoto.Lecz kiedy przyjdzie odebrać swe długi.Skończą się śmiechy, serdeczność, przysługi.W pęta Cię weźmie, zabierze bogactwo.Byś cierpiał katusze, aż gwiazdy zgasną172Ответить4 ответаBartosz KrotowskiBartosz Krotowski7 лет назадWitcher 3 is easily the best game of 2015 and RPG as well.826Ответить29 ответовMoise RareșMoise Rareș4 года назадI like how almost every soundtrack has a tint of master mirror's song tucked somewhere, just like the quests themselves. You just can't escape him...23ОтветитьLaniphLaniph7 лет назадThis is awesome. This game was breathtaking. I've never felt so implicated in a video game, and I've been playing for fifteen years. Andrzej Sapkowski is a great author, CD Projekt RED is a great studio, and together they made by far my favourite game. Thank you guys.293Ответить8 ответовNightlyNightly6 лет назадI felt like Hearts of Stone had more of an impact on me than Blood and Wine. Sure, Blood and Wine was depressing when I finished the game but Hearts of Stone had more of a long-lasting impact on me. Gaunter O'Dimm AKA Master Mirror was such a superb villain compared to Dettlaff (questionable villian?). His power brings the entire game to life.362Ответить13 ответовYijin YangYijin Yang6 лет назадMystery Man is such a wonderful piece of soundtrack.104Ответить3 ответаLaura VilsoneLaura Vilsone7 лет назадA good game keeps you entertained, a masterpiece sends shivers down your spine.38ОтветитьKyle HowardKyle Howard6 лет назадafter playing all 3 i personally think this expansion was the best story. it may have been shorter but everything about it was so amazing. o'dimm is my favorite villain of all time80Ответить1 ответMagMag5 лет назад0:00 to 2:55 lyrics His smile fair as spring, as towards him he draws you;His tongue sharp and silvery as he implores you.Your wishes he grants, as he swears to adore you,Gold, silver, jewels - he lays riches before you.Dues need be repaid and he will come for youAll to reclaim, no smile to console you.He'll snare you in bonds, eyes glowing afireTo gore and torment you till the stars expire!26ОтветитьLinus JiLinus Ji4 года назадThat feeling when O'Dimm walked down from the sky, and revealed we all stand on the moon....85Ответить4 ответаgj95gj956 лет назадEvil's Soft First Touches....there was never a more appropriate title for a song. It's so seductive, like it's whispering in your ear - tempting you with things beyond your wildest dreams, whatever you desire. Fame, riches, love, power, anything you could ever want, anything deep within your soul that you long for, can be yours. You know that maybe, this doesn't come without a price, and you can sense that this entity is not what it seems, not completely virtuous, that there is a darker side to this transaction. But the temptation is so strong, all your wishes waiting to be fulfilled - a fresh, juicy fruit dangling right in front of you, ready to be eaten. And all you have to do is take a bite...15Ответить"
        )
    }

    Scaffold(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        topBar = { NotePageHeader(navController, noteViewModel) },
        bottomBar = { NotePageFooter(navController, noteViewModel) },
        modifier = Modifier.imePadding().navigationBarsPadding().fillMaxSize(),
    ) { contentPadding ->
        BoxWithConstraints(Modifier.padding(contentPadding)) {
            textBoxHeight = constraints.maxHeight
            Column(
                Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                CustomTextField(
                    textFieldValue = textValue,
                    onValueChange = { textValue = it },
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                )
            }
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