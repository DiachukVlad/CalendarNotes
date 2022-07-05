package com.diachuk.calendarnotes.main


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diachuk.calendarnotes.list.ReusableList
import com.diachuk.calendarnotes.list.UniversalList
import com.diachuk.calendarnotes.selectableText.SelectableItem
import com.diachuk.calendarnotes.styledText.StyledUtil
import com.diachuk.routing.Route
import org.koin.androidx.compose.getViewModel

object MainRoute : Route() {
    @Composable
    override fun Content() {
        MainScreen()
    }
}

@Composable
fun MainScreen(vm: MainViewModel = getViewModel()) {
    val notes by vm.notes.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        vm.onLaunch()
    })

    UniversalList()

//    Scaffold(bottomBar = {
//        CBottomNavigation()
//    },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { vm.createNote() }) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = null)
//            }
//        }) {
//        LazyVerticalGrid(
//            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
//            columns = GridCells.Fixed(2)
//        ) {
//            items(notes.size) { index ->
//                Surface(
//                    elevation = 4.dp,
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .clickable {
//                            vm.noteClicked(index)
//                        }
//                ) {
//                    Column(Modifier.padding(8.dp)) {
//                        val note = notes[index]
//                        val text = if (note.text.length > 20) {
//                            note.text.substring(0..20) + "..."
//                        } else {
//                            note.text
//                        }
//
//                        val annotatedString = StyledUtil.createAnnotatedString(text, note.styles)
//
//                        Text(annotatedString, style = TextStyle(fontSize = 18.sp))
//                    }
//                }
//            }
//        }
//    }
}