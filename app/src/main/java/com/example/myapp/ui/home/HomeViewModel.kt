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

    private var currentPhotoPage = 1
    private var currentCollectionPage = 1

    private val _photoList = MutableStateFlow<UiState<List<PhotoResponse>>>(UiState.Loading)
    val photoList: StateFlow<UiState<List<PhotoResponse>>> = _photoList.asStateFlow()

    private val _collectionList =
        MutableStateFlow<UiState<List<CollectionResponse>>>(UiState.Loading)
    val collectionList: StateFlow<UiState<List<CollectionResponse>>> = _collectionList.asStateFlow()

    private val _isRefreshingPhoto = MutableStateFlow(false)
    val isRefreshingPhoto: StateFlow<Boolean> = _isRefreshingPhoto.asStateFlow()

    private val _isRefreshingCollection = MutableStateFlow(false)
    val isRefreshingCollection: StateFlow<Boolean> = _isRefreshingCollection.asStateFlow()

    init {
        fetchPhotos()
        getAllCollections()
    }

    fun refreshPhoto() {
        currentPhotoPage = 1
        fetchPhotos(isRefreshing = true)
    }

    fun refreshCollection() {
        currentCollectionPage = 1
        getAllCollections(isRefreshing = true)
    }

    fun sortPhotos(orderBy: String) {
        currentPhotoPage = 1
        fetchPhotos(isRefreshing = true, orderBy = orderBy)
    }


    fun fetchPhotos(isRefreshing: Boolean = false, orderBy: String = "latest") {
        viewModelScope.launch {

            if (!isRefreshing) {
                _photoList.value = UiState.Loading
            } else {
                _isRefreshingPhoto.value = true
            }

            val result = repository.getAllPhotos(currentPhotoPage, 20, orderBy)
            result.onSuccess {
                _photoList.value = UiState.Success(it)
                currentPhotoPage++
            }.onFailure {
                _photoList.value = UiState.Error(
                    "Lỗi không lấy được dữ liệu HomeViewModel: ${it.message}" ?: "Unknown error"
                )
            }

            _isRefreshingPhoto.value = false
        }
    }

    fun getAllCollections(isRefreshing: Boolean = false) {
        viewModelScope.launch {

            if (!isRefreshing) {
                _collectionList.value = UiState.Loading
            } else {
                _isRefreshingCollection.value = true
            }

            try {

                val result = collectionRepository.getAllCollections(currentCollectionPage, 20)

                result.onSuccess {
                    _collectionList.value = UiState.Success(it)
                    currentCollectionPage++
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
            } finally {
                _isRefreshingCollection.value = false
            }
        }
    }


}