package com.chondosha.flowerfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chondosha.flowerfinder.model.FlowerEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class FlowerListViewModel(
    private val repository: FlowerRepository
): ViewModel() {

    private val _flowers: MutableStateFlow<List<FlowerEntry>> = MutableStateFlow(emptyList())
    val flowers: StateFlow<List<FlowerEntry>>
        get() = _flowers.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFlowerEntries().collect {
                _flowers.value = it
            }
        }
    }

    suspend fun addFlowerEntry(flowerEntry: FlowerEntry) {
        repository.addFlowerEntry(flowerEntry)
    }
}

class FlowerListViewModelFactory(private val repository: FlowerRepository) : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowerListViewModel(repository) as T
    }
}