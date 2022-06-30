package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    controller: StyledController = remember { StyledController() },
    focusRequester: FocusRequester? = null
) {
    println("styled")
    var text by remember {
        mutableStateOf(TextFieldValue())
    }

    BasicTextField(
        value = text,
        onValueChange = {
            println("on change ${Random.nextInt()}")
            text = it
            controller.changed(it)
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 18.sp),
        visualTransformation = {
            TransformedText(controller.createAnnotatedString(it.text), OffsetMapping.Identity)
        }
    )
}