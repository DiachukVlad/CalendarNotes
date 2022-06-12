package com.diachuk.calendarnotes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.diachuk.calendarnotes.text.SelectableEditText
import com.diachuk.calendarnotes.text.SelectableItem


@Composable
fun ReusableList(
    texts: List<SelectableItem>,
    onValueChange: (index: Int, value: SelectableItem) -> Unit,
    onFocused: (index: Int) -> Unit = {},
    addNew: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column {
        texts.forEachIndexed { index, text ->
            SelectableEditText(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {
                    onValueChange(index, it)
                },
                keyboardActions = KeyboardActions(onNext = {
                    if (index < texts.lastIndex) {
                        texts[index+1].let { it.putCursorOn(it.textField.text.length) }
                        focusManager.moveFocus(FocusDirection.Down)
                    } else {
                        addNew()
                    }
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                style = MaterialTheme.typography.body2.copy(color = Color.DarkGray)
            )
        }
    }
}


