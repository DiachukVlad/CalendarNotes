package com.diachuk.calendarnotes.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

@Composable
fun MarkdownTextField() {
    var text by remember {
        mutableStateOf("qwe *ert* tyu rty")
    }

    val util = remember(key1 = text) {
        MarkdownUtil(text)
    }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxSize()
            .background(Color(252, 211, 3)),
        visualTransformation = {
            TransformedText(
                util.annotatedString,
                util.offsetMapping
            )
        }
    )
}