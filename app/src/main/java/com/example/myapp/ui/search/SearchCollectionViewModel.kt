package com.example.myapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.SearchCollectionResponse
import com.example.myapp.repository.SearchRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCollectionViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _result = MutableStateFlow<UiState<SearchCollectionResponse>>(UiState.Loading)
    val result: StateFlow<UiState<SearchCollectionResponse>> = _result

    fun searchCollections(query: String) {

        viewModelScope.launch {
            _result.value = UiState.Loading

            try {
                val resultQuery = searchRepository.searchCollections(query, 1, 20)
                resultQuery.onSuccess {
                    _result.value = UiState.Success(it)
                }.onFailure {
                    _result.value = UiState.Error(it.message.toString())
                }
            } catch (e: Exception) {
                _result.value = UiState.Error(e.message.toString())
            }
        }
    }

}