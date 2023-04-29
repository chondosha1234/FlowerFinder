package com.chondosha.flowerfinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class FlowerEntry(
    @PrimaryKey val id: UUID,
    val label: String,
    val percentage: Float,
    val date: Date,
    val wiki:String? = null,
    val photoFileName:String? = null
)
