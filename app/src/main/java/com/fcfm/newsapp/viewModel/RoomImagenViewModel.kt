package com.fcfm.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fcfm.newsapp.data.RoomImagen
import com.fcfm.newsapp.data.RoomImagenDAO
import kotlinx.coroutines.launch

class RoomImagenViewModel(private val roomImagenDAO: RoomImagenDAO): ViewModel() {
    val todasImagenes: LiveData<List<RoomImagen>> = roomImagenDAO.getImagenes().asLiveData()

    private fun insert(imagen: RoomImagen){
        viewModelScope.launch {
            roomImagenDAO.insert(imagen)
        }
    }

    fun borrarTabla(){
        viewModelScope.launch {
            roomImagenDAO.nukeTable()
        }
    }


    fun agregarImagen(id: String, imagen: String, noticiaId: String, v: Int){
        val nuevaImagen = RoomImagen(id, imagen, noticiaId, v)
        insert(nuevaImagen)
    }

    fun cargar(id: String): LiveData<RoomImagen>{
        return roomImagenDAO.getRoomImagen(id).asLiveData()
    }
}

class RoomImagenViewModelFactory(private val roomImagenDAO: RoomImagenDAO): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RoomImagenViewModel::class.java)){
            @Suppress("UNCHECKED CAST")
            return RoomImagenViewModel(roomImagenDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}