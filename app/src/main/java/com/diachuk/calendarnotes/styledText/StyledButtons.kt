package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.base.HSpace

@Composable
fun StyledButtons(
    modifier: Modifier = Modifier,
    controller: StyledController = remember { StyledController() }
) {
    Row(
        modifier = modifier.horizontalScroll(
            rememberScrollState()
        )
    ) {
        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Huge)
            }
        ) {
            Text("Huge")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Big)
            }
        ) {
            Text("Big")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Medium)
            }
        ) {
            Text("Medium")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Bold)
            }
        ) {
            Text("Bold")
        }

        HSpace(size = 8.dp)

        OutlinedButton(
            onClick = {
                controller.triggerStyle(StyleType.Italic)
            }
        ) {
            Text("Italic")
        }

        HSpace(size = 8.dp)
    }
}