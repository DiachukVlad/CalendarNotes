package com.diachuk.calendarnotes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import com.diachuk.calendarnotes.text.SelectableEditText

@Composable
fun ReusableList(controller: ListController = remember { ListController() }) {
    val texts = remember {
        mutableStateListOf(
            "One",
            "Two"
        )
    }
    var action by remember {
        mutableStateOf("None")
    }
    val focusManager = LocalFocusManager.current

    Column {
        Text(text = texts.size.toString())
        texts.forEachIndexed { index, s ->
            SelectableEditText(value = s, onValueChange = {
                texts[index] = it
            },
            onNext = {
                texts.add("NEWWWW")
                action = "New"
            })
        }
        Button(onClick = {
            texts.add("NEWWWW")
            action = "Newb"
        }) {
            Text(text = "+ add row")
        }
    }

    SideEffect {
        if (action == "New") {
            println("down")
            focusManager.moveFocus(FocusDirection.Down)
        }

        if (action != "None") {
            action = "None"
        }
    }
}


