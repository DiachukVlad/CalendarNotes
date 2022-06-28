package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StyledTextScreen() {
    val vm: StyledVM = remember { StyledVM() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            StyledButtons(
                modifier = Modifier.fillMaxWidth(),
                vm = vm
            )
        }
    ) {
        Column() {
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(vm)
        }
    }
}

@Composable
fun StyledTextField(vm: StyledVM = remember { StyledVM() }) {
    val textField by vm.textField.collectAsState()

    BasicTextField(
        value = textField,
        onValueChange = vm::changed,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        textStyle = TextStyle(fontSize = 24.sp)
    )
}