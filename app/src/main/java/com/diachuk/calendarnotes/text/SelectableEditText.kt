package com.diachuk.calendarnotes.text

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

data class SelectableItem(var text: TextFieldValue, var focused: Boolean = false)
fun String.toSelectable(): SelectableItem {
    return SelectableItem(TextFieldValue(this))
}

@Composable
fun SelectableEditText(
    value: SelectableItem,
    onValueChange: (SelectableItem) -> Unit,
    modifier: Modifier = Modifier,
    onFocused: (() -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    var editable by remember { mutableStateOf(false) }

    var clicked by remember {
        mutableStateOf(false)
    }
    val requester = remember {
        FocusRequester()
    }

    if (editable) {
        BasicTextField(
            value = value.text,
            onValueChange = {
                if (value.text != it) {
                    onValueChange(value.also {oldValue -> oldValue.text = it })
                }
            },
            textStyle = style,
            modifier = modifier
                .focusRequester(requester)
                .onFocusEvent {
                    if (value.focused && !it.isFocused) {
                        editable = false
                    }
                    value.focused = it.isFocused
                },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            visualTransformation = visualTransformation,
            cursorBrush = cursorBrush,
            maxLines = if (singleLine) 1 else maxLines,
        )
    } else {
        val onFocus = {
            onFocused?.invoke()
            editable = true
            clicked = true
        }
        var focused by remember {
            mutableStateOf(false)
        }

        ClickableText(modifier = modifier
            .onFocusChanged {
                if (!focused && it.isFocused) onFocus()
                focused = it.isFocused
            }
            .focusTarget()
            .defaultMinSize(minWidth = 10.dp, minHeight = 10.dp)
            .clickable {
                onFocus()
            },
            text = value.text.annotatedString,
            style = style,
            onClick = { offset ->
                onFocused?.invoke()
                editable = true
                clicked = true

                onValueChange(value.also{ it.text = value.text.copy(selection = TextRange(offset)) })
            }
        )
    }

    LaunchedEffect(key1 = clicked) {
        if (clicked) {
            requester.requestFocus()
            clicked = false
        }
    }

    LaunchedEffect(value.focused) {
        if(value.focused) {
            editable = true
            clicked = true
            value.focused = false
        }
    }
}