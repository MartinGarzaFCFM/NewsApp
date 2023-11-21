package com.fcfm.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.network.Usuario
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class UserViewModel: ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    private val _users = MutableLiveData<List<Usuario>>()

    val status: LiveData<ApiStatus> = _status
    val users: LiveData<List<Usuario>> = _users

    init {
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            ApiStatus.LOADING
            try {
                _users.value = NewsAppApi.retrofitService.getUsers()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _users.value = listOf()
            }

        }
    }
}