package com.diachuk.calendarnotes.markdown

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping

class MarkdownUtil(private val text: String) {
    // Pair<position, size>
    private val extraSymbols: MutableList<Pair<Int, Int>> = mutableListOf()
    var verb = true

    val offsetMapping: OffsetMapping =
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var delta = 0
                val indexF = extraSymbols.filter { it.first < offset }
                indexF.forEach { delta += it.second }

                if (verb)
                    println("ori = $offset -> ${offset-delta}")

                return 0.coerceAtLeast(offset - delta).coerceAtMost(newText.length-1)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var delta = 0
                for (extraS in extraSymbols) {
                    if (extraS.first - delta > offset) break
                    delta += extraS.second
                }

                if (verb)
                    println("tra = $offset -> ${offset+delta}")
                return offset + delta
            }
        }

    var newText = ""
    val annotatedString: AnnotatedString =
        buildAnnotatedString {
            val result = ArrayList<Pair<SequenceStyle, MatchResult>>()

            patterns.forEach { seq ->
                seq.regex.findAll(text).forEach {
                    result.add(seq to it)
                    extraSymbols.add(it.range.first to seq.sequenceSize)
                    if (seq.closable) {
                        extraSymbols.add(it.range.last - seq.sequenceSize + 1 to seq.sequenceSize)
                    }
                }
            }

            newText = text
            extraSymbols.sortedByDescending { it.first }.forEach {
                newText = newText.removeRange(it.first until it.first+it.second)
            }
            append(newText)

            println(extraSymbols)

            verb = false
            result.forEach {(seq, it) ->
                addStyle(
                    seq.style, offsetMapping.originalToTransformed(
                        it.range.first
                    ),
                    offsetMapping.originalToTransformed(
                        it.range.last + 1
                    )
                )
            }
            verb = true
        }


    private fun getSymbolPosInLine(cursor: Int, symbol: Char): Int {
        for (i in cursor until text.length) {
            if (text[i] == symbol) return i
            else if (text[i] == '\n') return -1
        }
        return -1
    }
}