package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow

enum class StyleType {
    H1, H2, H3, Bold, Italic
}

data class StyleRange(var styleTypes: HashSet<StyleType>, var start: Int, var end: Int)

fun StyleType.createStyle(): SpanStyle {
    return when (this) {
        StyleType.H1 -> SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
        StyleType.H2 -> TODO()
        StyleType.H3 -> TODO()
        StyleType.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
        StyleType.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
    }
}

sealed class ChangeEvent {
    data class CursorPaste(val str: String) : ChangeEvent()
    data class CursorDelete(val str: String) : ChangeEvent()

    data class SelectionPaste(val str: String) : ChangeEvent()
    data class SelectionDelete(val str: String) : ChangeEvent()

    object SelectionChange : ChangeEvent()

    fun isNewLine(): Boolean {
        return (this is CursorPaste && str == "\n")
    }

    fun isBackspace(): Boolean {
        return this is CursorDelete && str.length == 1
    }
}


class StyledVM {
    val textField = MutableStateFlow(TextFieldValue("qasdjkhg\nkljwesdf\nsdklfj"))
    private var annotatedString: AnnotatedString
        get() = textField.value.annotatedString
        set(value) {
            textField.tryEmit(textField.value.copy(annotatedString = value))
        }
    private val cursor: Int?
        get() = textField.value.cursor

    val print = MutableStateFlow("None")

    private val styles = mutableListOf<StyleRange>()

    private fun whatEvent(tf: TextFieldValue): ChangeEvent {
        val delta = tf.annotatedString.text.length - annotatedString.text.length

        if (cursor != null && tf.cursor != null) {
            when {
                delta > 0 -> return ChangeEvent.CursorPaste(
                    tf.text.substring(
                        cursor!!,
                        tf.cursor!!
                    )
                )
                delta < 0 -> return ChangeEvent.CursorDelete(
                    textField.value.text.substring(
                        tf.cursor!!,
                        cursor!!
                    )
                )
            }
        }

        if (cursor == null && tf.cursor != null) {
            val selectionSize = textField.value.selection.end - textField.value.selection.start
            when {
                delta == -selectionSize -> return ChangeEvent.SelectionDelete(
                    textField.value.text.substring(
                        textField.value.selection.start,
                        textField.value.selection.end
                    )
                )
                delta != 0 -> return ChangeEvent.SelectionPaste(
                    tf.text.substring(
                        textField.value.selection.start,
                        tf.cursor!!
                    )
                )
            }
        }

        return ChangeEvent.SelectionChange
    }

    fun changed(tf: TextFieldValue) {
        val event = whatEvent(tf)

        if (event is ChangeEvent.CursorPaste || event is ChangeEvent.CursorDelete) {
            val delta =
                when (event) {
                    is ChangeEvent.CursorPaste -> event.str.length
                    is ChangeEvent.CursorDelete -> -event.str.length
                    else -> 0
                }

            updateRanges(cursor!!, delta, event.isNewLine())

            textField.tryEmit(tf)
            update()
            return
        }

        textField.tryEmit(tf)
        update()
    }

    private fun updateRanges(cursor: Int, delta: Int, isNewLine: Boolean) {
        val reachedStyle =
            styles.firstOrNull { (it.start == it.end && it.end == cursor) || (it.start < cursor && it.end >= cursor) }
        val afterStyles =
            styles.filter { it.start > cursor || (it.start == cursor && it.start != it.end) }

        if (reachedStyle != null && !isNewLine) {
            reachedStyle.end += delta
        }
        afterStyles.forEach {
            it.start += delta
            it.end += delta
        }
    }

    fun onClick(styleType: StyleType) {
        var start = textField.value.selection.start
        var end = textField.value.selection.end

        if (styles.size == 0) {
            styles.add(StyleRange(hashSetOf(styleType), start, end))
            changed(textField.value)
            return
        }

        var tmpStyle = styles[0]
        val firstNotNull: List<StyleRange>.(predicate: (StyleRange) -> Boolean) -> Boolean = {
            val first = firstOrNull(it)

            if (first == null) {
                false
            } else {
                tmpStyle = first
                true
            }
        }

        var goodRange = true
        while (goodRange) {
            when {
                // equal start, equal end
                // ----- style
                // ===== selection
                styles.firstNotNull { it.start == start && it.end == end } -> {
                    goodRange = false
                    tmpStyle.triggerStyle(styleType)
                }
                // equal start, end inside
                // ----- style
                // ===-- selection
                styles.firstNotNull { it.start == start && end in (it.start + 1) until it.end } -> {
                    goodRange = false
                    styles.add(StyleRange(HashSet(tmpStyle.styleTypes), end, tmpStyle.end))
                    tmpStyle.end = end
                    tmpStyle.triggerStyle(styleType)
                }
                // start inside, equal end
                // ----- style
                // --=== selection
                styles.firstNotNull { it.end == end && start in (it.start + 1) until it.end } -> {
                    goodRange = false
                    styles.add(StyleRange(HashSet(tmpStyle.styleTypes), tmpStyle.start, start))
                    tmpStyle.start = start
                    tmpStyle.triggerStyle(styleType)
                }
                // start outside, equal end
                //   ----- style
                // ======= selection
                styles.firstNotNull { it.start in (start + 1) until end && end == it.end } -> {
                    end = tmpStyle.start
                    tmpStyle.styleTypes.add(styleType)
                }
                // equal start, end outside
                // -----   style
                // ======= selection
                styles.firstNotNull { it.end in (start + 1) until end && start == it.start } -> {
                    start = tmpStyle.end
                    tmpStyle.styleTypes.add(styleType)
                }
                // start inside, end inside
                // -----   style
                //  ===    selection
                styles.firstNotNull { start > it.start && end < it.end } -> {
                    goodRange = false
                    val copyCenter = tmpStyle.copy(
                        styleTypes = HashSet(tmpStyle.styleTypes),
                        start = start,
                        end = end
                    ).also { it.triggerStyle(styleType) }

                    val copyEnd =
                        tmpStyle.copy(styleTypes = HashSet(tmpStyle.styleTypes), start = end)

                    tmpStyle.end = start

                    styles.add(copyCenter)
                    styles.add(copyEnd)
                }
                // start outside, end outside
                //  -----  style
                //    ==== selection
                styles.firstNotNull { start in (it.start + 1) until it.end && end > it.end } -> {
                    styles.add(
                        StyleRange(
                            HashSet(tmpStyle.styleTypes + styleType),
                            start,
                            tmpStyle.end
                        )
                    )
                    val tmp = start
                    start = tmpStyle.end
                    tmpStyle.end = tmp
                }
                else -> {
                    goodRange = false
                    styles.add(StyleRange(hashSetOf(styleType), start, end))
                }
            }
        }

        styles.sortBy { it.start }

        styles.removeAll { it.styleTypes.size == 0 }

        changed(textField.value)
    }

    private fun StyleRange.triggerStyle(styleType: StyleType) {
        if (styleTypes.contains(styleType)) {
            styleTypes.remove(styleType)
        } else {
            styleTypes.add(styleType)
        }
    }

    private fun update() {
        annotatedString = buildAnnotatedString {
            append(annotatedString.text)

            styles
                .filter { it.start != it.end }
                .forEach {
                    var sumStyle = SpanStyle()
                    it.styleTypes.forEach {
                        sumStyle = sumStyle.merge(it.createStyle())
                    }

                    addStyle(sumStyle, start = it.start, end = it.end)
                }
        }
    }
}