package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.charBeforeCursor(): Char? {
    val cursor = selection.start
    return if (cursor == 0) return null else text[cursor - 1]
}

val TextFieldValue.cursor: Int?
    get() {
        if (selection.start != selection.end) return null
        return selection.start
    }

fun TextFieldValue.copyWithCursorOn(cursor: Int): TextFieldValue {
    return copy(selection = TextRange(cursor, cursor))
}
fun TextFieldValue.copyWithCursorOnLast(): TextFieldValue {
    return copy(selection = TextRange(text.length, text.length))
}