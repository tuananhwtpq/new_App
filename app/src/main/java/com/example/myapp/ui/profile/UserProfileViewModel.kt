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

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _isLoadingLikePhoto = MutableStateFlow(false)
    val isLoadingLikePhoto: StateFlow<Boolean> = _isLoadingLikePhoto.asStateFlow()

    private var currentPhotoPage = 1
    private var currentLikePhotoPage = 1

    init {

        Log.d("UserProfileViewModel", "Username: $username")

        if (username.isNotEmpty()) {
            getUserProfile(username)
            getUserListPhoto(username)
            getUserLikePhoto(username)
        }
    }

    //region LOAD MORE PHOTO
    fun loadMorePhoto() {
        if (_isLoadingMore.value || userListPhoto.value is UiState.Loading) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            getUserListPhoto(username)
            _isLoadingMore.value = false
        }
    }

    //region LOAD MORE LIKE PHOTO
    fun loadMoreLikePhoto() {
        if (_isLoadingLikePhoto.value || userLikePhoto.value is UiState.Loading) return

        viewModelScope.launch {
            _isLoadingLikePhoto.value = true
            getUserLikePhoto(username)
            _isLoadingLikePhoto.value = false
        }
    }


    //region GET USER PROFILE
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

    //region GET USER LIST PHOTO
    fun getUserListPhoto(username: String) {
        viewModelScope.launch {
            if (currentPhotoPage == 1) {
                _userListPhoto.value = UiState.Loading
            }

            try {
                val response = userRepository.getUserPhotos(username, currentPhotoPage, 20)
                response.onSuccess { newPhotos ->

                    if (currentPhotoPage == 1) {
                        _userListPhoto.value = UiState.Success(newPhotos)
                    } else {
                        val currentList =
                            (_userListPhoto.value as? UiState.Success)?.data ?: emptyList()
                        _userListPhoto.value = UiState.Success(currentList + newPhotos)
                    }
                    currentPhotoPage++
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

    //region GET USER LIKE PHOTO
    fun getUserLikePhoto(username: String) {
        viewModelScope.launch {
            if (currentLikePhotoPage == 1) {
                _userLikePhoto.value = UiState.Loading
            }

            try {
                val response =
                    userRepository.getUserLikePhoto(username, currentLikePhotoPage, perPage = 20)
                response.onSuccess { newLikedPhotos ->

                    if (currentLikePhotoPage == 1) {
                        _userLikePhoto.value = UiState.Success(newLikedPhotos)
                    } else {
                        val currentList =
                            (_userLikePhoto.value as? UiState.Success)?.data ?: emptyList()
                        _userLikePhoto.value = UiState.Success(currentList + newLikedPhotos)

                    }
                    currentLikePhotoPage++
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