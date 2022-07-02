package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.sp
import com.diachuk.calendarnotes.styledText.StyledUtil.createAnnotatedString

@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    controller: StyledController = remember { StyledController() },
    focusRequester: FocusRequester? = null
) {
    val text by controller.textField.collectAsState()

    BasicTextField(
        value = text,
        onValueChange = {
            controller.changed(it)
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 18.sp),
        visualTransformation = {
            TransformedText(
                createAnnotatedString(it.text, controller.styles),
                OffsetMapping.Identity
            )
        }
    )
}