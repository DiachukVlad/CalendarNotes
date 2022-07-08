package com.diachuk.calendarnotes.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {
    @TypeConverter
    fun noteComponentToList(data: String?): List<NoteComponentEntity> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<NoteComponentEntity?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun listToNoteComponent(someObjects: List<NoteComponentEntity?>?): String {
        return Gson().toJson(someObjects)
    }
}