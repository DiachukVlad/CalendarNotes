package com.diachuk.calendarnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.list.ReusableList
import com.diachuk.calendarnotes.text.SelectableItem
import com.diachuk.calendarnotes.text.toSelectable
import com.diachuk.calendarnotes.ui.theme.CalendarNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val focusManager = LocalFocusManager.current
            CalendarNotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            focusManager.clearFocus(true)
                        },
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    val texts = remember {
        mutableStateListOf(
            "Onew".toSelectable(),
            "asdjkh".toSelectable(),
            "qweoij".toSelectable()
        )
    }

    SelectionContainer {
        Column {
            Button(onClick = {
                texts+=SelectableItem(TextFieldValue(), true)
            }) {
                Text(text = "Add new")
            }

            Spacer(modifier = Modifier.size(10.dp))

            ReusableList(texts = texts, onValueChange = { index, text -> texts[index] = text })
        }
    }
}
