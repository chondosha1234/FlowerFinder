package com.chondosha.flowerfinder.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chondosha.flowerfinder.model.FlowerEntry
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface FlowerDao {

    @Query("SELECT * FROM FlowerEntry")
    fun getFlowerEntries(): Flow<List<FlowerEntry>>

    @Query("SELECT * FROM FlowerEntry WHERE id=(:id)")
    suspend fun getFlowerEntry(id: UUID): FlowerEntry

    @Insert
    suspend fun addFlowerEntry(flowerEntry: FlowerEntry)

    @Delete
    suspend fun deleteFlowerEntry(flowerEntry: FlowerEntry)
}