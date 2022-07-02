package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.base.HSpace
import kotlin.experimental.and

@Composable
fun StyledButtons(
    modifier: Modifier = Modifier,
    controller: StyledController = remember { StyledController() }
) {
    val selectedByte by controller.selectedByte.collectAsState()

    Row(
        modifier = modifier.horizontalScroll(
            rememberScrollState()
        )
    ) {
        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Huge)
            },
            modifier = Modifier.background(if ((selectedByte and StyleType.Huge.byte) > 0) Color.Red else Color.White)
        ) {
            Text("Huge")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Big)
            },
            modifier = Modifier.background(if ((selectedByte and StyleType.Big.byte) > 0) Color.Red else Color.White)
        ) {
            Text("Big")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Medium)
            },
            modifier = Modifier.background(if ((selectedByte and StyleType.Medium.byte) > 0) Color.Red else Color.White)
        ) {
            Text("Medium")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Bold)
            },
            modifier = Modifier.background(if ((selectedByte and StyleType.Bold.byte) > 0) Color.Red else Color.White)
        ) {
            Text("Bold")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Italic)
            },
            modifier = Modifier.background(if ((selectedByte and StyleType.Italic.byte) > 0) Color.Red else Color.White)
        ) {
            Text("Italic")
        }

        HSpace(size = 8.dp)
    }
}