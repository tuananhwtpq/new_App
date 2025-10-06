package com.example.myapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.repository.HomeItemDetailRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: HomeItemDetailRepository
) : ViewModel() {

    private val _imageInfo = MutableStateFlow<UiState<PhotoResponse>>(UiState.Loading)
    val imageInfo: StateFlow<UiState<PhotoResponse>> = _imageInfo.asStateFlow()


    fun getPhotoById(id: String) {
        viewModelScope.launch {
            _imageInfo.value = UiState.Loading

            val result = repo.getPhotoById(id)
            //Log.d(TAG, "Kết quả ID: $id")

            result.onSuccess {
                _imageInfo.value = UiState.Success(it)
                Log.d("HomeItemDetailViewModel", "Du lieu: ${it}")
            }
                .onFailure {
                    _imageInfo.value = UiState.Error(it.message ?: "Unknown Error")
                    Log.e("HomeItemDetailViewModel", "Lỗi lấy dữ liệu ảnh: ${it.message}")
                }
        }
    }


}