package com.example.myapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.SearchUserResponse
import com.example.myapp.data.model.UserWithPhoto
import com.example.myapp.repository.SearchRepository
import com.example.myapp.repository.UserRepository
import com.example.myapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _searchUserResult = MutableStateFlow<UiState<List<UserWithPhoto>>>(UiState.Loading)
    val searchUserResult: StateFlow<UiState<List<UserWithPhoto>>> = _searchUserResult.asStateFlow()

    private val _isLoadingMore = MutableStateFlow<Boolean>(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentUserPage = 1
    private var currentQuery = ""


    fun searchUser(query: String) {
        // Chỉ thực hiện tìm kiếm mới nếu query thay đổi
        if (query.isBlank() || query == currentQuery) return

        currentQuery = query
        currentUserPage = 1
        _searchUserResult.value = UiState.Loading
        fetchUsersInternal()
    }

    fun loadMoreUsers() {
        if (_isLoadingMore.value || _searchUserResult.value !is UiState.Success) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentUserPage++
            fetchUsersInternal()
            _isLoadingMore.value = false
        }
    }

    private fun fetchUsersInternal() {
        viewModelScope.launch {
            try {
                val result = repository.searchUsers(currentQuery, currentUserPage, 20)

                result.onSuccess { userSearchResponse ->
                    val userWithPhotos = userSearchResponse.results.map { user ->
                        async {
                            val photoResult = userRepo.getUserPhotos(user.username.toString(), 1, 3)
                            val photos = photoResult.getOrNull() ?: emptyList()
                            UserWithPhoto(user = user, photos = photos)
                        }
                    }.awaitAll()

                    if (currentUserPage == 1) {
                        _searchUserResult.value = UiState.Success(userWithPhotos)
                    } else {
                        val currentList =
                            (_searchUserResult.value as? UiState.Success)?.data ?: emptyList()
                        _searchUserResult.value = UiState.Success(currentList + userWithPhotos)
                    }
                }.onFailure {
                    _searchUserResult.value = UiState.Error(it.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                if (currentUserPage == 1) {
                    _searchUserResult.value = UiState.Error(e.message.toString())
                }
            }
        }
    }

}