package com.plop.plopmessenger.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.of("+9")) }
    }

    @TypeConverter
    fun dateToTimestamp(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.atZone(ZoneOffset.of("+9"))?.toInstant()?.toEpochMilli()
    }
}