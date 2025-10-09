package com.example.myapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.SearchPhotoResponse
import com.example.myapp.repository.SearchRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow<UiState<SearchPhotoResponse>>(UiState.Init)
    val searchResult: StateFlow<UiState<SearchPhotoResponse>> = _searchResult

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private var currentQuery = ""


    fun searchPhotos(query: String) {
        if (query != currentQuery) {
            currentPage = 1
            currentQuery = query
            _searchResult.value = UiState.Loading
        }

        fetchPhotosInternal()
    }

    fun loadMorePhotos() {
        if (_isLoadingMore.value || searchResult.value is UiState.Loading) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentPage++
            fetchPhotosInternal()
            _isLoadingMore.value = false
        }
    }

//    fun searchPhotos(query: String) {
//        viewModelScope.launch {
//            _searchResult.value = UiState.Loading
//            repository.searchPhotos(query, 1, 20)
//                .onSuccess { _searchResult.value = UiState.Success(it) }
//                .onFailure { _searchResult.value = UiState.Error(it.message.toString()) }
//        }
//    }

    private fun fetchPhotosInternal() {
        viewModelScope.launch {
            repository.searchPhotos(currentQuery, currentPage, 20)
                .onSuccess { newResponse ->
                    if (currentPage == 1) {
                        _searchResult.value = UiState.Success(newResponse)
                    } else {
                        val currentResponse = (_searchResult.value as? UiState.Success)?.data
                        if (currentResponse != null) {
                            val updatedResults = currentResponse.results + newResponse.results
                            val updatedResponse = currentResponse.copy(results = updatedResults)
                            _searchResult.value = UiState.Success(updatedResponse)
                        }
                    }
                }
                .onFailure { _searchResult.value = UiState.Error(it.message.toString()) }
        }
    }

}