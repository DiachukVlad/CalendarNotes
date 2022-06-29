package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

enum class StyleType(val byte: Byte) {
    None(0), Huge(0b1), Big(0b10), Medium(0b100), Bold(0b1000), Italic(0b10000)
}


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
}


class StyledController {
    val textField = MutableStateFlow(TextFieldValue(""))
    private var annotatedString: AnnotatedString
        get() = textField.value.annotatedString
        set(value) {
            textField.tryEmit(textField.value.copy(annotatedString = value))
        }
    private val cursor: Int?
        get() = textField.value.cursor

    private val styles =
        ArrayList<Byte>().also { it.addAll(Array(annotatedString.length) { StyleType.None.byte }) }
    private var cursorStyle: Byte? = null

    private fun whatEvent(tf: TextFieldValue): ChangeEvent {
        val delta = tf.annotatedString.text.length - annotatedString.text.length

        if (cursor != null && tf.cursor != null) {
            when {
                delta > 0 -> return ChangeEvent.Paste(cursor!!, delta)
                delta < 0 -> return ChangeEvent.Delete(tf.cursor!! to cursor!!)
            }
        }

        if (cursor == null && tf.cursor != null) {
            val selectionSize = textField.value.selection.end - textField.value.selection.start
            when {
                delta == -selectionSize -> return ChangeEvent.Delete(textField.value.selection.start to textField.value.selection.end)
                delta != 0 -> return ChangeEvent.DeletePaste(
                    textField.value.selection.start to textField.value.selection.end,
                    tf.cursor!! - textField.value.selection.start
                )
            }
        }

        return ChangeEvent.SelectionChange
    }

    fun changed(tf: TextFieldValue) {
        when (val event = whatEvent(tf)) {
            is ChangeEvent.Paste -> {
                val byte: Byte =
                    if (cursorStyle != null) {
                        cursorStyle!!
                    } else if (event.length == 1 && tf.text[tf.cursor!! - 1] == '\n') {
                        StyleType.None.byte
                    } else if (event.cursor - 1 >= 0) {
                        styles[event.cursor - 1]
                    } else {
                        StyleType.None.byte
                    }

                styles.addAll(event.cursor, Array(event.length) { byte }.toList())
            }
            is ChangeEvent.Delete -> {
                for (i in (event.range.first until event.range.second).reversed()) {
                    styles.removeAt(i)
                }
            }
            is ChangeEvent.DeletePaste -> {
                for (i in (event.deleteRange.first until event.deleteRange.second).reversed()) {
                    styles.removeAt(i)
                }
                val byte: Byte =
                    if (cursorStyle != null) {
                        cursorStyle!!
                    } else if (event.deleteRange.first - 1 >= 0) {
                        styles[event.deleteRange.first - 1]
                    } else {
                        StyleType.None.byte
                    }
                styles.addAll(
                    event.deleteRange.first,
                    Array(event.pasteSize) { byte }.toList()
                )
            }
            ChangeEvent.SelectionChange -> {}
        }

        cursorStyle = null

        textField.tryEmit(tf)
        update()
    }


    fun triggerStyle(styleType: StyleType) {
        val start = textField.value.selection.start
        val end = textField.value.selection.end

        if (start == end) {
            if (start > 0) {
                cursorStyle = (cursorStyle ?: StyleType.None.byte) or styles[start - 1]
            }
            cursorStyle = (cursorStyle ?: StyleType.None.byte) xor styleType.byte
        } else if (styles.subList(start, end).all { it and styleType.byte > 0 }) {
            for (i in start until end) {
                styles[i] = styles[i] xor styleType.byte
            }
        } else {
            for (i in start until end) {
                styles[i] = styles[i] or styleType.byte
            }
        }

        update()
    }

    private fun update() {
        annotatedString = buildAnnotatedString {
            append(annotatedString.text)

            styles.forEachIndexed { index, byte ->
                addStyle(byte.createStyle(), index, index + 1)
            }
        }
    }
}