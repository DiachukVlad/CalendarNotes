package com.diachuk.calendarnotes.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.diachuk.calendarnotes.data.CheckList
import com.diachuk.calendarnotes.data.CheckListItem
import com.diachuk.calendarnotes.styledText.copyWithCursorOnLast
import kotlinx.coroutines.flow.MutableStateFlow

class CheckListController() {
    var focusManager: FocusManager? = null

    val items = mutableStateListOf(
        CheckListItemForController(TextFieldValue(" ", selection = TextRange(1, 1)), false),
    )
    val focusIndex = MutableStateFlow(-1)

    var onRemovedLastElement: () -> Unit = {}

    private var id: Long = -1

    constructor(checkList: CheckList) : this() {
        items.clear()
        items.addAll(checkList.items.map {
            CheckListItemForController(
                TextFieldValue(it.text),
                checked = it.checked
            )
        })
        id = checkList.id
    }

    fun generateCheckList(): CheckList {
        return CheckList(
            items = items.map {
                CheckListItem(text = it.textField.text, checked = it.checked)
            },
            id = id
        )
    }

    fun onCheckChanged(index: Int, checked: Boolean) {
        items[index] = items[index].copy(checked = checked)
    }

    fun onTextChanged(index: Int, textField: TextFieldValue) {
        if (textField.text.isEmpty()) {
            if (items.size > index) {
                items.removeAt(index)
                focusManager?.moveFocus(FocusDirection.Up)
                if (index > 0) {
                    items[index - 1] = items[index - 1].copy(
                        textField = items[index - 1].textField.copy(
                            selection = TextRange(
                                items[index - 1].textField.text.length,
                                items[index - 1].textField.text.length
                            )
                        )
                    )
                }

                if (items.isEmpty()) onRemovedLastElement()
            }
        } else if (textField.text.first() == ' ') {
            if (textField.selection.start == 0) {
                val start = textField.selection.start.coerceAtLeast(1)
                val end = textField.selection.end.coerceAtLeast(1)
                items[index] = items[index].copy(
                    textField = items[index].textField.copy(
                        selection = TextRange(
                            start,
                            end
                        )
                    )
                )
            } else {
                items[index] = items[index].copy(textField = textField)
            }
        }
    }

    fun onNextEvent(index: Int) {
        if (index < items.lastIndex) {
            items[index + 1] =
                items[index + 1].copy(textField = items[index + 1].textField.copyWithCursorOnLast())
            focusManager?.moveFocus(FocusDirection.Down)
        } else {
            items.add(
                CheckListItemForController(
                    TextFieldValue(" ", selection = TextRange(1, 1)),
                    false
                )
            )
            focusIndex.tryEmit(items.lastIndex)
        }
    }
}
