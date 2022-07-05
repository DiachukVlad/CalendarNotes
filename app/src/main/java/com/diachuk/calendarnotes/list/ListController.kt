package com.diachuk.calendarnotes.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import com.diachuk.calendarnotes.selectableText.SelectableItem

class ListController {
    val textsState = mutableStateListOf<SelectableItem>()

    fun onChange(index: Int, item: SelectableItem) {
        textsState[index] = item
    }
}
