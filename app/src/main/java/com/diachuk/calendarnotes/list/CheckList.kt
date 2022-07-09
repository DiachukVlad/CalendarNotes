package com.diachuk.calendarnotes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

data class CheckListItemForController(val textField: TextFieldValue, val checked: Boolean)

@Composable
fun CheckList(controller: CheckListController) {
    controller.focusManager = LocalFocusManager.current

    val focusIndex by controller.focusIndex.collectAsState()

    Column {
        controller.items.forEachIndexed { index, value ->
            Row {
                Checkbox(
                    checked = value.checked,
                    onCheckedChange = { controller.onCheckChanged(index, it) })
                FocusableTextField(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    value = value.textField,
                    onValueChange = {
                        controller.onTextChanged(index, it)
                    },
                    keyboardActions = KeyboardActions(onNext = {
                        controller.onNextEvent(index)
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    focused = index == focusIndex
                )
            }
        }
    }
}

@Composable
fun FocusableTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focused: Boolean = false
) {
    val focusRequester = remember {
        FocusRequester()
    }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
    )

    LaunchedEffect(key1 = focused, block = {
        if (focused) {
            focusRequester.requestFocus()
        }
    })
}