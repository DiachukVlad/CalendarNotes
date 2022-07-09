package com.diachuk.calendarnotes.selectableText

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SelectableEditText(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    requester: FocusRequester = remember { FocusRequester() },
    onLostFocus: () -> Unit = {},
    onGetFocus: (offset: Int)->Unit = {},
    style: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    var wasFocused by remember {
        mutableStateOf(false)
    }

    if (editable) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (value != it) {
                    onValueChange(it)
                }
            },
            textStyle = style,
            modifier = modifier
                .focusRequester(requester)
                .onFocusEvent {
                    if (wasFocused && !it.isFocused) {
                        onLostFocus()
                    }
                    wasFocused = it.isFocused
                },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            visualTransformation = visualTransformation,
            cursorBrush = cursorBrush,
            maxLines = if (singleLine) 1 else maxLines,
        )
    } else {
        ClickableText(modifier = modifier
            .onFocusChanged {
                if (it.isFocused) {
                    onGetFocus(-1)
                }
            }
            .focusTarget()
            .defaultMinSize(minWidth = 10.dp, minHeight = 10.dp),
            text = value.annotatedString,
            style = style,
            onClick = { offset ->
                onGetFocus(offset)
            }
        )
    }

    LaunchedEffect(editable) {
        if (editable) {
            requester.requestFocus()
        }
    }
}