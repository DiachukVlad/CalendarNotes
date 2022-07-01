package com.diachuk.calendarnotes.note

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diachuk.calendarnotes.base.HSpace
import com.diachuk.calendarnotes.base.VSpace
import com.diachuk.calendarnotes.styledText.StyledButtons
import com.diachuk.calendarnotes.styledText.StyledTextField
import com.diachuk.routing.Route
import org.koin.androidx.compose.getViewModel

object NoteRoute: Route(enterTransition = slideInHorizontally{it/2} + fadeIn(), exitTransition = slideOutHorizontally{it/2} + fadeOut()) {
    @Composable
    override fun Content() {
        NoteScreen()
    }
}

@Preview
@Composable
fun NoteScreen(vm: NoteViewModel = getViewModel()) {
    val title by vm.title.collectAsState()
    val dateText by vm.dateText.collectAsState()

    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                StyledButtons(controller = vm.styledController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = vm::onDoneClick) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
                .fillMaxSize()
        ) {
            // Title
            BasicTextField(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp),
                value = title,
                onValueChange = vm::changeTitle,
                textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium)
            )

            VSpace(size = 24.dp)

            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                HSpace(size = 6.dp)
                Text(dateText)
            }

            VSpace(size = 16.dp)

            Divider(
                Modifier
                    .padding(start = 8.dp, end = 48.dp)
                    .fillMaxWidth()
            )

            VSpace(size = 16.dp)

            println("Before")
            StyledTextField(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
                    .fillMaxSize()
                    .padding(start = 16.dp),
                vm.styledController
            )
        }
    }
}