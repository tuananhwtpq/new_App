package com.example.myapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.repository.CollectionRepository
import com.example.myapp.repository.HomeRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _photoList = MutableStateFlow<UiState<List<PhotoResponse>>>(UiState.Loading)
    val photoList: StateFlow<UiState<List<PhotoResponse>>> = _photoList.asStateFlow()

    private val _collectionList =
        MutableStateFlow<UiState<List<CollectionResponse>>>(UiState.Loading)
    val collectionList: StateFlow<UiState<List<CollectionResponse>>> = _collectionList.asStateFlow()


    private var currentPage = 1

    init {
        fetchPhotos()
        getAllCollections()
    }

    fun fetchPhotos() {
        viewModelScope.launch {
            _photoList.value = UiState.Loading

            val result = repository.getAllPhotos(currentPage, 100)
            result.onSuccess {
                _photoList.value = UiState.Success(it)
                currentPage++
            }.onFailure {
                _photoList.value = UiState.Error(
                    "Lỗi không lấy được dữ liệu HomeViewModel: ${it.message}" ?: "Unknown error"
                )
            }
        }
    }

    fun getAllCollections() {
        viewModelScope.launch {
            _collectionList.value = UiState.Loading

            try {

                val result = collectionRepository.getAllCollections(currentPage, 20)

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