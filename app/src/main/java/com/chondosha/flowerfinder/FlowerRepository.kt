package com.chondosha.flowerfinder

import android.content.Context
import androidx.room.Room
import com.chondosha.flowerfinder.model.FlowerEntry
import com.chondosha.flowerfinder.model.database.FlowerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import java.util.*

private const val DATABASE_NAME = "flower-database"

class FlowerRepository(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope ) {

    private val database: FlowerDatabase = Room.databaseBuilder(
        context.applicationContext,
        FlowerDatabase::class.java,
        DATABASE_NAME
    ).build()

    fun getFlowerEntries(): Flow<List<FlowerEntry>> = database.flowerDao().getFlowerEntries()

    suspend fun getFlowerEntry(id: UUID): FlowerEntry = database.flowerDao().getFlowerEntry(id)

    suspend fun addFlowerEntry(flowerEntry: FlowerEntry) = database.flowerDao().addFlowerEntry(flowerEntry)

    suspend fun deleteFlowerEntry(flowerEntry: FlowerEntry) = database.flowerDao().deleteFlowerEntry(flowerEntry)

}