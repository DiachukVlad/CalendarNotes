package com.diachuk.calendarnotes.main


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diachuk.calendarnotes.data.Styled
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

    Scaffold(bottomBar = {
        CBottomNavigation()
    },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.createNote() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }) {
        LazyVerticalGrid(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            columns = GridCells.Fixed(2)
        ) {
            items(notes.size) { index ->
                Surface(
                    elevation = 4.dp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            vm.noteClicked(index)
                        }
                ) {
                    Column(Modifier.padding(8.dp)) {
                        val note = notes[index]

                        val styled = (note.components.firstOrNull { it is Styled } as Styled?)
                        val text = styled?.text ?: "Empty"

                        Text(text, style = TextStyle(fontSize = 18.sp))
                    }
                }
            }
        }
    }
}