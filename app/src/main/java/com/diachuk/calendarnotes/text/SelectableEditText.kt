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
import kotlin.random.Random

@Composable
fun SelectableEditText(
    value: String,
    onValueChange: (String) -> Unit,
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
    LaunchedEffect(Unit) {
        println(Random.nextInt())
    }
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    SelectableEditText(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        modifier,
        onFocused,
        style,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        visualTransformation,
        cursorBrush
    )
}

@Composable
fun SelectableEditText(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
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
    var wasFocused by remember {
        mutableStateOf(false)
    }
    val requester = remember {
        FocusRequester()
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
                        editable = false
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
            text = value.annotatedString,
            style = style,
            onClick = { offset ->
                onFocused?.invoke()
                editable = true
                clicked = true

                onValueChange(value.copy(selection = TextRange(offset)))
            }
        )
    }

    LaunchedEffect(key1 = clicked) {
        if (clicked) {
            requester.requestFocus()
            clicked = false
        }
    }
}