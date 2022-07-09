package com.diachuk.calendarnotes.note

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.base.HSpace
import com.diachuk.calendarnotes.base.VSpace
import com.diachuk.calendarnotes.list.CheckList
import com.diachuk.calendarnotes.list.CheckListController
import com.diachuk.calendarnotes.styledText.StyledController
import com.diachuk.calendarnotes.styledText.StyledTextField
import com.diachuk.routing.Route
import org.koin.androidx.compose.getViewModel

class NoteRoute(private val id: Long?) :
    Route(enterTransition = slideInHorizontally { it / 2 } + fadeIn(),
        exitTransition = slideOutHorizontally { it / 2 } + fadeOut()) {
    @Composable
    override fun Content() {
        NoteScreen(id)
    }
}

@Composable
fun NoteScreen(id: Long?, vm: NoteViewModel = getViewModel()) {
    val dateText by vm.dateText.collectAsState()

    LaunchedEffect(key1 = id) {
        vm.id = id
    }

    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
//                StyledButtons(controller = vm.styledController)
            }
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = vm::addCheckList) {
                    Icon(
                        imageVector = Icons.Default.Checklist,
                        contentDescription = "Add checklist"
                    )
                }
                FloatingActionButton(onClick = vm::onDoneClick) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
                .fillMaxSize()
        ) {
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

            Column(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                vm.controllers.forEach { controller ->
                    when (controller) {
                        is StyledController -> StyledTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            controller
                        )
                        is CheckListController -> CheckList(controller = controller)
                    }
                }
            }


        }
    }
}