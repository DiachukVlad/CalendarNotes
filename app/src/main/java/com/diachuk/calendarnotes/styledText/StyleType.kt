package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.experimental.and
import kotlin.experimental.or

enum class StyleType(val byte: Byte) {
    None(0), Huge(0b1), Big(0b10), Medium(0b100), Bold(0b1000), Italic(0b10000)
}
val sizeByte get()= StyleType.Huge.byte or StyleType.Big.byte or StyleType.Medium.byte


fun Byte.createStyle(): SpanStyle {
    var res = SpanStyle()

    if (this and StyleType.Huge.byte > 0) {
        res += SpanStyle(fontSize = 32.sp)
    }
    if (this and StyleType.Big.byte > 0) {
        res += SpanStyle(fontSize = 28.sp)
    }
    if (this and StyleType.Medium.byte > 0) {
        res += SpanStyle(fontSize = 24.sp)
    }
    if (this and StyleType.Bold.byte > 0) {
        res += SpanStyle(fontWeight = FontWeight.Bold)
    }
    if (this and StyleType.Italic.byte > 0) {
        res += SpanStyle(fontStyle = FontStyle.Italic)
    }

    return res
}

sealed class ChangeEvent {
    data class Paste(val cursor: Int, val length: Int) : ChangeEvent()
    data class Delete(val range: Pair<Int, Int>) : ChangeEvent()
    data class DeletePaste(val deleteRange: Pair<Int, Int>, val pasteSize: Int) : ChangeEvent()
    object SelectionChange : ChangeEvent()
    object Nothing : ChangeEvent()
}
