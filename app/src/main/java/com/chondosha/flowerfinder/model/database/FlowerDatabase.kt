package com.chondosha.flowerfinder.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chondosha.flowerfinder.model.FlowerEntry

@Database(entities = [FlowerEntry::class], version=1)
@TypeConverters(FlowerTypeConverters::class)
abstract class FlowerDatabase: RoomDatabase() {
    abstract fun flowerDao(): FlowerDao
}