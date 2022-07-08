package com.diachuk.calendarnotes.data.base

import androidx.room.PrimaryKey

open class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}