package com.diachuk.calendarnotes.styledText

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun StyledButtons(modifier: Modifier = Modifier, vm: StyledVM = remember { StyledVM() }) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier) {
        StyledButton(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        "H1"
                    )
                }
            },
            onClick = {
                vm.onClick(StyleType.Huge)
            }
        )
        StyledButton(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        "Bold"
                    )
                }
            },
            onClick = {
                vm.onClick(StyleType.Bold)
            }
        )
        StyledButton(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(
                        "Italic"
                    )
                }
            },
            onClick = {
                vm.onClick(StyleType.Italic)
            }
        )
    }
}

@Composable
fun StyledButton(text: AnnotatedString, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(2.dp)
            .size(40.dp)
            .background(Color.Gray)
            .clickable(onClick = onClick),
        textAlign = TextAlign.Center
    )
}