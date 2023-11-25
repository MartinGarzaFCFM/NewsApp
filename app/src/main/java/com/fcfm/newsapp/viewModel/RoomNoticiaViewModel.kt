package com.fcfm.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fcfm.newsapp.data.RoomNoticia
import com.fcfm.newsapp.data.RoomNoticiaDAO
import kotlinx.coroutines.launch

class RoomNoticiaViewModel(private val roomNoticiaDAO: RoomNoticiaDAO): ViewModel() {
    val allNoticias: LiveData<List<RoomNoticia>> = roomNoticiaDAO.getNoticias().asLiveData()


    private val _status = MutableLiveData<ApiStatus>()
    private val _noticias = MutableLiveData<List<RoomNoticia>>()

    val status: LiveData<ApiStatus> = _status
    val noticias: LiveData<List<RoomNoticia>> = _noticias

    private fun insertNoticia(noticia: RoomNoticia){
        viewModelScope.launch {
            roomNoticiaDAO.insert(noticia)
        }
    }

    fun borrarTablaNoticias(){
        viewModelScope.launch {
            roomNoticiaDAO.nukeTable()
        }
    }

    fun isNoticiasEmpty(): Boolean{
        return roomNoticiaDAO.isEmpty()
    }

    private fun crearNuevaNoticia(id: String, titulo: String, subtitulo: String, categoria: String,
                                  cuerpo: String, autor: String, aprobado: Boolean, creadoEn: String,
                                  modificadoEn: String, v: Int): RoomNoticia
    {
        return RoomNoticia(_id = id, title = titulo, subtitle = subtitulo, category = categoria,
            body = cuerpo, author = autor, approved = aprobado, createdAt = creadoEn,
            updatedAt = modificadoEn, __v = v)
    }

    fun agregarNuevaNoticia(id: String,titulo: String, subtitulo: String, categoria: String,
                            cuerpo: String, autor: String, aprobado: Boolean, creadoEn: String,
                            modificadoEn: String, v: Int)
    {
        val nuevaNoticia = crearNuevaNoticia(id, titulo, subtitulo, categoria, cuerpo, autor,
            aprobado, creadoEn, modificadoEn, v)

        insertNoticia(nuevaNoticia)
    }

    fun cargarNoticia(id: String): LiveData<RoomNoticia>{
        return roomNoticiaDAO.getRoomNoticia(id).asLiveData()
    }

    fun actualizarNoticia(roomNoticia: RoomNoticia){
        viewModelScope.launch {
            roomNoticiaDAO.update(roomNoticia)
        }
    }

    fun borrarNoticia(roomNoticia: RoomNoticia){
        viewModelScope.launch {
            roomNoticiaDAO.delete(roomNoticia)
        }
    }
}

class RoomNoticiaViewModelFactory(private val roomNoticiaDAO: RoomNoticiaDAO) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomNoticiaViewModel::class.java)){
            @Suppress("UNCHECKED CAST")
            return RoomNoticiaViewModel(roomNoticiaDAO) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}