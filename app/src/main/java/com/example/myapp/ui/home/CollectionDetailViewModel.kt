package com.example.myapp.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.Logger
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.repository.CollectionRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val _collectionList =
        MutableStateFlow<UiState<List<PhotoResponse>>>(UiState.Loading)
    val collectionList: StateFlow<UiState<List<PhotoResponse>>> = _collectionList.asStateFlow()

    private val collectionId = saveStateHandle.get<String>("collectionId")

    init {
        if (collectionId != null) {
            fetchCollections()
        }
    }

    fun fetchCollections() {

        viewModelScope.launch {
            _collectionList.value = UiState.Loading

            try {
                val collections =
                    collectionRepository.getCollectionPhotos(
                        id = collectionId.toString(),
                        page = 1,
                        perPage = 20
                    )

                collections.onSuccess {
                    _collectionList.value = UiState.Success(it)
                }.onFailure {
                    _collectionList.value = UiState.Error(it.message ?: "Unknown error")
                    Log.e("CollectionDetailViewModel", "Error fetching collections: ${it.message}")
                }
            } catch (e: Exception) {
                _collectionList.value = UiState.Error(e.message ?: "Unknown error")
            }
        }

    }
}