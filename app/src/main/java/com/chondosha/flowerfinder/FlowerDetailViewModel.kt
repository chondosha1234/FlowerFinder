package com.chondosha.flowerfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chondosha.flowerfinder.model.FlowerEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class FlowerDetailViewModel(
    private val repository: FlowerRepository,
    flowerId: String?
): ViewModel() {

    private val _flower: MutableStateFlow<FlowerEntry?> = MutableStateFlow(null)
    val flower: StateFlow<FlowerEntry?> = _flower.asStateFlow()

    init {
        val flower = UUID.fromString(flowerId)
        viewModelScope.launch {
            _flower.value = repository.getFlowerEntry(flower)
        }
    }

    suspend fun deleteFlowerEntry(flowerEntry: FlowerEntry) {
        repository.deleteFlowerEntry(flowerEntry)
    }
}

class FlowerDetailViewModelFactory(
    private val repository: FlowerRepository,
    private val flowerId: String?
    ) : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowerDetailViewModel(repository, flowerId) as T
    }
}