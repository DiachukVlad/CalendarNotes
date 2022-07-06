package com.diachuk.calendarnotes.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.diachuk.calendarnotes.styledText.copyWithCursorOnLast
import kotlinx.coroutines.flow.MutableStateFlow

class CheckListController {
    var focusManager: FocusManager? = null

    val items = mutableStateListOf(
        CheckListItem(TextFieldValue(" asdkljasd"), true),
        CheckListItem(TextFieldValue(" qwe erty qw"), false),
        CheckListItem(TextFieldValue(" qwe erty qw"), false)
    )
    val focusIndex = MutableStateFlow(-1)


    fun onCheckChanged(index: Int, checked: Boolean) {
        items[index] = items[index].copy(checked = checked)
    }

    fun onTextChanged(index: Int, textField: TextFieldValue) {
        if (textField.text.isEmpty()) {
            if (items.size > index) {
                items.removeAt(index)
                focusManager?.moveFocus(FocusDirection.Up)
            }
        } else if (textField.text.first() == ' ') {
            if (textField.selection.start == 0) {
                val start = textField.selection.start.coerceAtLeast(1)
                val end = textField.selection.end.coerceAtLeast(1)
                items[index] = items[index].copy(textField = items[index].textField.copy(selection = TextRange(start, end)))
            } else {
                items[index] = items[index].copy(textField = textField)
            }
        }
    }

    fun onNextEvent(index: Int) {
        if (index < items.lastIndex) {
            items[index + 1] = items[index + 1].copy(textField = items[index + 1].textField.copyWithCursorOnLast())
            focusManager?.moveFocus(FocusDirection.Down)
        } else {
            items.add(CheckListItem(TextFieldValue(" ", selection = TextRange(1, 1)), false))
            focusIndex.tryEmit(items.lastIndex)
        }
    }
}
