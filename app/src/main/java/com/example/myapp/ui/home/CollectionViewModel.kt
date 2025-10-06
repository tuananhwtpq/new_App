package com.example.myapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.repository.CollectionRepository
import com.example.myapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CollectionViewModel @Inject constructor(
    private val repository: CollectionRepository
) : ViewModel() {

    private val _collectionList =
        MutableStateFlow<UiState<List<CollectionResponse>>>(UiState.Loading)
    val collectionList: StateFlow<UiState<List<CollectionResponse>>> = _collectionList.asStateFlow()

    private var currentPage = 1

    init {
        getAllCollections()
    }

    fun getAllCollections() {
        viewModelScope.launch {
            _collectionList.value = UiState.Loading

            try {

                val result = repository.getAllCollections(currentPage, 20)

                result.onSuccess {
                    _collectionList.value = UiState.Success(it)
                    currentPage++
                }
                    .onFailure {
                        _collectionList.value = UiState.Error(
                            "Lỗi không lấy được dữ liệu CollectionViewModel: ${it.message}"
                                ?: "Unknown error"
                        )
                    }

            } catch (e: Exception) {
                _collectionList.value =
                    UiState.Error("Lỗi không lấy được dữ liệu CollectionViewModel: ${e.message}")
            }
        }
    }
}