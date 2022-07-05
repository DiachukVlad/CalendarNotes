package com.diachuk.calendarnotes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.selectableText.SelectableItem
import com.diachuk.calendarnotes.selectableText.toSelectable

enum class DotType {
    Dot, Checkbox
}

data class ListItem(
    val selectableItem: SelectableItem,
    val checked: Boolean = false
)

@Composable
fun UniversalList(dotType: DotType = DotType.Dot) {
    val listItems: MutableList<ListItem> =
        remember { mutableStateListOf(ListItem(" asdasd".toSelectable())) }

    val addNew: () -> Unit = {
        listItems.add(
            ListItem(
                SelectableItem.empty(focused = true)
            )
        )
    }

    val focusManager = LocalFocusManager.current

    Surface(modifier = Modifier.padding(8.dp), elevation = 4.dp, shape = RoundedCornerShape(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Button(onClick = addNew) {
                Text(text = "Add new")
            }

            SelectionContainer {
                ReusableList(
                    texts = listItems.map { it.selectableItem },
                    onValueChange = { index, value ->
                        listItems[index] = listItems[index].copy(selectableItem = value)
                    },
                    enterOnLast = addNew,
                    dotContent = {
                        Surface(
                            modifier = Modifier
                                .size(16.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .align(CenterHorizontally),
                            color = Color.Red
                        ) {}
                    },
                    onRemove = {
                        if (listItems.size > it) {
                            listItems.removeAt(it)
                            focusManager.moveFocus(FocusDirection.Up)
                        }
                    }
                )
            }
        }
    }
}