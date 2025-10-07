package com.example.myapp.ui.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User
import com.example.myapp.repository.UserRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UiState<User>>(UiState.Loading)
    val userProfile: StateFlow<UiState<User>> = _userProfile.asStateFlow()

    private val _userListPhoto = MutableStateFlow<UiState<List<PhotoResponse>>>(UiState.Loading)
    val userListPhoto: StateFlow<UiState<List<PhotoResponse>>> = _userListPhoto.asStateFlow()

    private val _userLikePhoto = MutableStateFlow<UiState<List<PhotoResponse>>>(UiState.Loading)
    val userLikePhoto: StateFlow<UiState<List<PhotoResponse>>> = _userLikePhoto.asStateFlow()

    private val username: String = savedStateHandle.get<String>("username") ?: ""

    init {

        Log.d("UserProfileViewModel", "Username: $username")

        if (username.isNotEmpty()) {
            getUserProfile(username)
            getUserListPhoto(username, 1)
            getUserLikePhoto(username, 1)
        }
    }

    fun getUserProfile(username: String) {
        viewModelScope.launch {
            _userProfile.value = UiState.Loading

            try {

                val response = userRepository.getUserProfile(username)
                response.onSuccess {
                    _userProfile.value = UiState.Success(it)
                }
                    .onFailure {
                        _userProfile.value = UiState.Error(
                            "Lỗi không lấy được dữ liệu người dùng: ${it.message}"
                                ?: "Lỗi không xác định"
                        )
                    }

            } catch (e: Exception) {
                _userProfile.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getUserListPhoto(username: String, page: Int) {
        viewModelScope.launch {
            _userListPhoto.value = UiState.Loading

            try {
                val response = userRepository.getUserPhotos(username, page)
                response.onSuccess {
                    _userListPhoto.value = UiState.Success(it)
                }
                    .onFailure {
                        _userListPhoto.value = UiState.Error(
                            "Lỗi không lấy được dữ liệu: ${it.message}"
                                ?: "Lỗi không xác định"
                        )
                    }

            } catch (e: Exception) {
                _userListPhoto.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getUserLikePhoto(username: String, page: Int) {
        viewModelScope.launch {
            _userLikePhoto.value = UiState.Loading

            try {
                val response = userRepository.getUserLikePhoto(username, page)
                response.onSuccess {
                    _userLikePhoto.value = UiState.Success(it)
                }
                    .onFailure {
                        _userLikePhoto.value = UiState.Error(
                            "Lỗi không lấy được dữ liệu: ${it.message}"
                        )
                    }

            } catch (e: Exception) {
                _userLikePhoto.value = UiState.Error(e.message.toString())
            }
        }
    }


}