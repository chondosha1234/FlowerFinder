package com.chondosha.flowerfinder.model.database

import androidx.room.TypeConverter
import java.util.*

class FlowerTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long{
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}