package com.fcfm.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.network.NoticiaFromAPI
import kotlinx.coroutines.launch

class NoticiaViewModel: ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    private val _noticias = MutableLiveData<List<NoticiaFromAPI>>()

    val status: LiveData<ApiStatus> = _status
    val noticias: LiveData<List<NoticiaFromAPI>> = _noticias

    init {
        getNoticias()
    }

    private fun getNoticias() {
        viewModelScope.launch {
            ApiStatus.LOADING
            try {
                _noticias.value = NewsAppApi.retrofitService.getNoticias()
                _status.value = ApiStatus.DONE
            }catch (e: Exception){
                _noticias.value = listOf()
                _status.value = ApiStatus.ERROR
            }
        }
    }
}