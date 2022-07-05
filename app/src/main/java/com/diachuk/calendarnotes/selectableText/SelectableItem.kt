package com.diachuk.calendarnotes.selectableText

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

open class SelectableItem(var textField: TextFieldValue) {
    var focused: Boolean by mutableStateOf(false)

    constructor(textField: TextFieldValue, focused: Boolean) : this(textField) {
        this.focused = focused
    }

    fun putCursorOn(offset: Int) {
        textField = textField.copy(selection = TextRange(offset))
    }

    fun copy(textField: TextFieldValue): SelectableItem {
        return SelectableItem(textField = textField, focused = focused)
    }

    override fun toString(): String {
        return "SelectableItem(text=$textField, focused=$focused)"
    }

    companion object {
        fun empty(focused: Boolean = false): SelectableItem {
            return SelectableItem(TextFieldValue(" ", TextRange(1,1))).also { it.focused = focused }
        }
    }
}

fun String.toSelectable(): SelectableItem {
    return SelectableItem(TextFieldValue(this))
}