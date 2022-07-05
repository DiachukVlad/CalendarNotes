package com.diachuk.calendarnotes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import com.diachuk.calendarnotes.selectableText.SelectableEditText
import com.diachuk.calendarnotes.selectableText.SelectableItem


@Composable
fun ReusableList(
    texts: List<SelectableItem>,
    onValueChange: (index: Int, value: SelectableItem) -> Unit,
    onFocused: (index: Int) -> Unit = {},
    enterOnLast: () -> Unit = {},
    onRemove: (index: Int) -> Unit = {},
    dotContent: @Composable () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column {
        texts.forEachIndexed { index, text ->
            Row() {
                dotContent()
                SelectableEditText(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        if (it.textField.text.isEmpty()) {
                            onRemove(index)
                        } else if(it.textField.text.first() == ' '){
                            if (it.textField.selection.start == 0) {
                                val start = it.textField.selection.start.coerceAtLeast(1)
                                val end = it.textField.selection.end.coerceAtLeast(1)
                                onValueChange(index, it.copy(it.textField.copy(selection = TextRange(start, end))))
                            } else {
                                onValueChange(index, it)
                            }
                        }
                    },
                    keyboardActions = KeyboardActions(onNext = {
                        if (index < texts.lastIndex) {
                            texts[index + 1].let { it.putCursorOn(it.textField.text.length) }
                            focusManager.moveFocus(FocusDirection.Down)
                        } else {
                            enterOnLast()
                        }
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    style = MaterialTheme.typography.body2.copy(color = Color.DarkGray),
                )
            }
        }
    }
}



