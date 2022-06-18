package com.diachuk.calendarnotes.markdown

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class SequenceStyle(val regex: Regex, val sequenceSize: Int, val closable: Boolean, val q: String, val style: SpanStyle)

val patterns = arrayOf(
    SequenceStyle(
        Regex("#(?!#).+(\n|$)"),
        sequenceSize = 1,
        closable = false,
        q = "#",
        style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
    ),
    SequenceStyle(
        Regex("##(?!#).+(\n|$)"),
        sequenceSize = 2,
        closable = false,
        q = "##",
        style = SpanStyle(fontSize = 21.sp, fontWeight = FontWeight.Bold)
    ),
    SequenceStyle(
        Regex("###.+(\n|$)"),
        sequenceSize = 3,
        closable = false,
        q = "###",
        style = SpanStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    ),
    SequenceStyle(
        Regex("(?<!\\*)\\*[^*]+\\*(?!\\*)"),
        sequenceSize = 1,
        closable = true,
        q = "* *",
        style = SpanStyle(fontFamily = FontFamily.Cursive)
    ),
    SequenceStyle(
        Regex("(?<!\\*)\\*\\*[^*]+\\*\\*(?!\\*)"),
        sequenceSize = 2,
        closable = true,
        q = "** **",
        style = SpanStyle(fontWeight = FontWeight.Bold)
    ),
)