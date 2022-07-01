package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
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
    val text by controller.textField.collectAsState()

//    DisposableEffect(key1 = controller, effect = {
//        controller.onChange = {
//            text = text.copy(selection = text.selection.run { return@run TextRange(end, end)})
//        }
//        onDispose {
//            controller.onChange = {}
//        }
//    })

    BasicTextField(
        value = text,
        onValueChange = {
            controller.changed(it)
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 18.sp),
        visualTransformation = {
            TransformedText(controller.createAnnotatedString(it.text), OffsetMapping.Identity)
        }
    )
}