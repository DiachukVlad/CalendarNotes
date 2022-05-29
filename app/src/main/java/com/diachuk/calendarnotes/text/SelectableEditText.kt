package com.diachuk.calendarnotes.text

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.zIndex

@Composable
fun SelectableEditText(
    value: String,
    onValueChange: (String) -> Unit,
    onNext: (() -> Unit)? = null
) {
    var editable by remember { mutableStateOf(false) }
    val requester = remember {
        FocusRequester()
    }
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    Box {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValueState = it
                if (value != it.text) {
                    onValueChange(it.text)
                }
            },
            textStyle = LocalTextStyle.current.copy(color = Color.Gray),
            modifier = Modifier
                .focusRequester(requester)
                .alpha(if (editable) 1f else 0f)
                .zIndex(if (editable) 1f else 0f)
                .onFocusChanged {
                    if (!it.isFocused) {
                        editable = false
                    }
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext?.invoke()
                }
            ),
            singleLine = true
        )

        ClickableText(
            textFieldValue.annotatedString,
            style = LocalTextStyle.current.copy(color = Color.Gray),
            modifier = Modifier
                .alpha(if (!editable) 1f else 0f)
                .zIndex(if (!editable) 1f else 0f)
                .focusable(true),
            onClick = { offset ->
                Log.d("ClickableText", "$offset -th character is clicked.")
                editable = true
                requester.requestFocus()

                textFieldValueState = TextFieldValue(
                    text = textFieldValue.text,
                    selection = TextRange(offset)
                )
                if (value != textFieldValue.text) {
                    onValueChange(textFieldValue.text)
                }
            }
        )
    }
}