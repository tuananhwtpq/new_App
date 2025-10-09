package com.example.myapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.SearchCollectionResponse
import com.example.myapp.repository.SearchRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCollectionViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _result = MutableStateFlow<UiState<SearchCollectionResponse>>(UiState.Loading)
    val result: StateFlow<UiState<SearchCollectionResponse>> = _result

    private val _isLoadingMore = MutableStateFlow<Boolean>(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private var currentQuery = ""


    fun seachCollections(query: String) {
        if (query != currentQuery) {
            currentPage = 1
            currentQuery = query
            _result.value = UiState.Loading
        }
        fetchCollectionsInternal()
    }

    fun loadMoreCollections() {
        if (_isLoadingMore.value || result.value is UiState.Loading) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentPage++
            fetchCollectionsInternal()
            _isLoadingMore.value = false
        }
    }

    private fun fetchCollectionsInternal() {
        viewModelScope.launch {
            searchRepository.searchCollections(currentQuery, currentPage, 20)
                .onSuccess { newResponse ->
                    if (currentPage == 1) {
                        _result.value = UiState.Success(newResponse)
                    } else {
                        val currentResponse = (_result.value as UiState.Success).data
                        if (currentResponse != null) {
                            val updateResult = currentResponse.results + newResponse.results
                            val updateResponse = currentResponse.copy(results = updateResult)
                            _result.value = UiState.Success(updateResponse)
                        }
                    }
                }
                .onFailure {
                    _result.value = UiState.Error(it.message.toString())
                }
        }
    }

}