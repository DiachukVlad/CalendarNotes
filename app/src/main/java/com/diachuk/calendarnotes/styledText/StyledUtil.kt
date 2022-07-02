package com.diachuk.calendarnotes.styledText

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import java.lang.StrictMath.min

object StyledUtil {
    fun createAnnotatedString(text: String, styles: List<Byte>): AnnotatedString {
        return buildAnnotatedString {
            append(text)

            if (styles.isEmpty()) return@buildAnnotatedString

            var start = 0
            var lastByte = styles.first()
            val lastIndex = min(styles.size, text.length)

            for(i in 1 until lastIndex) {
                val byte = styles[i]
                if (byte != lastByte) {
                    addStyle(lastByte.createStyle(), start, i)

                    start = i
                    lastByte = byte
                }
            }

            addStyle(lastByte.createStyle(), start, lastIndex)
        }
    }
}