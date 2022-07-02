package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Integer.min
import java.lang.StrictMath.max
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor


class StyledController {
    var textField = MutableStateFlow(TextFieldValue(""))
    var selectedByte = MutableStateFlow<Byte>(0b0)

    private val annotatedString: AnnotatedString
        get() = textField.value.annotatedString

    private val cursor: Int?
        get() = textField.value.cursor

    val styles =
        ArrayList<Byte>().also { it.addAll(Array(annotatedString.length) { StyleType.None.byte }) }
    private var cursorStyle: Byte? = null

    private fun whatEvent(tf: TextFieldValue): ChangeEvent {
        if (tf.text == textField.value.text && tf.selection == textField.value.selection) {
            return ChangeEvent.Nothing
        }

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
            ChangeEvent.Nothing -> {
                textField.tryEmit(tf)
                return
            }
        }

        textField.tryEmit(tf)
        changeSelectedByte()
        cursorStyle = null
    }

    fun triggerStyle(styleType: StyleType) {
        val start: Int = textField.value.selection.let { min(it.start, it.end) }
        val end: Int = textField.value.selection.let { max(it.start, it.end) }

        if (start == end) {
            if (start > 0) {
                cursorStyle = (cursorStyle ?: StyleType.None.byte) or styles[start - 1]
            }
            cursorStyle = (cursorStyle ?: StyleType.None.byte) xor styleType.byte
        } else if (styles.subList(start, end).all { it and styleType.byte > 0 }) {
            if ((styleType.byte and sizeByte) != 0.toByte()) {
                for (i in start until end) {
                    styles[i] = (styles[i] or sizeByte) xor sizeByte
                    styles[i] = styles[i] xor styleType.byte
                }
            } else {
                for (i in start until end) {
                    styles[i] = styles[i] xor styleType.byte
                }
            }
        } else {
            if ((styleType.byte and sizeByte) != 0.toByte()) {
                for (i in start until end) {
                    styles[i] = (styles[i] or sizeByte) xor sizeByte
                    styles[i] = styles[i] or styleType.byte
                }
            } else {
                for (i in start until end) {
                    styles[i] = styles[i] or styleType.byte
                }
            }
        }

        val composition =
            if (textField.value.composition?.let { it.start == start && it.end == end } == true) {
                null
            } else {
                TextRange(
                    start,
                    end
                )
            }
        textField.tryEmit(textField.value.copy(composition = composition))
        changeSelectedByte()
    }

    private fun changeSelectedByte() {
        val start: Int = textField.value.selection.let { min(it.start, it.end) }
        val end: Int = textField.value.selection.let { max(it.start, it.end) }

        if (start == end) {
            val byte = cursorStyle ?: if (start == 0) StyleType.None.byte else styles[start-1]
            selectedByte.tryEmit(byte)
        } else {
            var resByte: Byte = 0x7f
            for(i in start until end) {
                resByte = resByte and styles[i]
            }
            selectedByte.tryEmit(resByte)
        }
    }
}