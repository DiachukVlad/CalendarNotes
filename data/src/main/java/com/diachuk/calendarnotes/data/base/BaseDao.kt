package com.diachuk.calendarnotes.data.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T : BaseEntity>(private val tableName: String) {
    @Insert
    abstract fun insert(entity: T): Long

    @Insert
    abstract fun insert(entities: List<T>): List<Long>

    @Update
    abstract fun update(entity: T)

    @Update
    abstract fun update(entities: List<T>)

    @Delete
    abstract fun delete(entity: T)

    @Delete
    abstract fun delete(entities: List<T>)

    @RawQuery
    protected abstract fun getEntitySync(query: SupportSQLiteQuery): List<T>?

    fun getEntityById(id: Long): T? {
        return getEntitiesByIds(listOf(id))?.firstOrNull()
    }

    fun getEntitiesByIds(ids: List<Long>): List<T>? {
        val result = StringBuilder()
        for (index in ids.indices) {
            if (index != 0) {
                result.append(",")
            }
            result.append("'").append(ids[index]).append("'")
        }
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName WHERE id IN ($result);")
        return getEntitySync(query)
    }

    fun getAll(): List<T>? {
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName;")
        return getEntitySync(query)
    }
}