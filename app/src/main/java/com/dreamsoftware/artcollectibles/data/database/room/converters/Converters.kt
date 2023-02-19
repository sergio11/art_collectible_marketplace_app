package com.dreamsoftware.artcollectibles.data.database.room.converters

import androidx.room.TypeConverter
import java.util.*

/**
 * All Converters for save entities
 */
class Converters {

    /**
     * From Timestamp
     * @param value
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    /**
     * Date to Timestamp
     * @param date
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun stringToList(value: String): List<String> =
        value.split(",")

    @TypeConverter
    fun listToString(list: List<String>): String =
        list.joinToString(",")
}