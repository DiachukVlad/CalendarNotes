package com.diachuk.calendarnotes.list

import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import com.diachuk.calendarnotes.text.CTextField

data class CheckItem(var text: String, var isChecked: Boolean = false)

@Composable
fun CheckList(
    title: String,
    items: List<CheckItem>,
    onValueChange: (index: Int, value: CheckItem) -> Unit,
    onAddNew: ()->Unit
) {
    var addNewFocused by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    Column {
        Text(text = title, style = MaterialTheme.typography.h4)
        items.forEachIndexed { index, item ->
            CTextField(
                value = item.text,
                onValueChange = { onValueChange(index, item.copy(text = it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }


        Text(text = "Add new",
            Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        onAddNew()
                    }
                    addNewFocused = it.isFocused
                }
                .focusTarget()
                .clickable(onClick = {
                    onAddNew()
                })
                .background(if (addNewFocused) Color.Blue else Color.White)
        )
    }

    LaunchedEffect(key1 = items.size) {
        if (items.last().text.isEmpty() && addNewFocused) {
            focusManager.moveFocus(FocusDirection.Up)
        }
    }
}