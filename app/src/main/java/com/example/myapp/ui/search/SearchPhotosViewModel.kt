package com.example.myapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.SearchPhotoResponse
import com.example.myapp.repository.SearchRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow<UiState<SearchPhotoResponse>>(UiState.Init)
    val searchResult: StateFlow<UiState<SearchPhotoResponse>> = _searchResult

    fun searchPhotos(query: String) {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading
            repository.searchPhotos(query, 1, 20)
                .onSuccess { _searchResult.value = UiState.Success(it) }
                .onFailure { _searchResult.value = UiState.Error(it.message.toString()) }
        }
    }
}