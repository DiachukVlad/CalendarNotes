package com.diachuk.calendarnotes.text

import android.view.textclassifier.TextSelection
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

data class SelectableItem(var text: TextFieldValue) {
    var focused: Boolean by mutableStateOf(false)
    override fun toString(): String {
        return "SelectableItem(text=$text, focused=$focused)"
    }

    companion object {
        fun empty(focused: Boolean = false): SelectableItem {
            return SelectableItem(TextFieldValue()).also { it.focused = focused }
        }
    }
}

fun String.toSelectable(): SelectableItem {
    return SelectableItem(TextFieldValue(this))
}

@Composable
fun SelectableEditText(
    value: SelectableItem,
    onValueChange: (SelectableItem) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    val requester = remember {
        FocusRequester()
    }
    var wasFocused by remember {
        mutableStateOf(false)
    }

    if (value.focused) {
        BasicTextField(
            value = value.text,
            onValueChange = {
                if (value.text != it) {
                    value.text = it
                    onValueChange(value)
                }
            },
            textStyle = style,
            modifier = modifier
                .focusRequester(requester)
                .onFocusEvent {
                    if (wasFocused && !it.isFocused) {
                        value.focused = false
                        wasFocused = false
                    }
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
                    value.focused = true
                }
            }
            .focusTarget()
            .defaultMinSize(minWidth = 10.dp, minHeight = 10.dp),
            text = value.text.annotatedString,
            style = style,
            onClick = { offset ->
                value.focused = true
                value.text = value.text.copy(selection = TextRange(offset))
                onValueChange(value)
            }
        )
    }

    LaunchedEffect(value.focused) {
        if(value.focused) {
            requester.requestFocus()
        }
    }
}