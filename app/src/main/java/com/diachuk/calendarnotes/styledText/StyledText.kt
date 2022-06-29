package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    controller: StyledController = remember { StyledController() },
    focusRequester: FocusRequester? = null
) {
    val textField by controller.textField.collectAsState()

    BasicTextField(
        value = textField,
        onValueChange = controller::changed,
        modifier = focusRequester?.let { modifier.focusRequester(focusRequester) } ?: modifier,
        textStyle = TextStyle(fontSize = 18.sp)
    )
}