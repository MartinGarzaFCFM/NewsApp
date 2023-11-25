package com.fcfm.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fcfm.newsapp.adapter.NoticiaListAdapter
import com.fcfm.newsapp.data.RoomApplication
import com.fcfm.newsapp.databinding.FragmentHomeBinding
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.viewModel.NoticiaViewModel
import com.fcfm.newsapp.viewModel.RoomImagenViewModel
import com.fcfm.newsapp.viewModel.RoomImagenViewModelFactory
import com.fcfm.newsapp.viewModel.RoomNoticiaViewModel
import com.fcfm.newsapp.viewModel.RoomNoticiaViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private val viewModel: NoticiaViewModel by viewModels()

    private val roomViewModel: RoomNoticiaViewModel by activityViewModels {
        RoomNoticiaViewModelFactory(
            (activity?.application as RoomApplication).database.roomNoticiaDao()
        )
    }

    private val imagenesViewModel: RoomImagenViewModel by activityViewModels {
        RoomImagenViewModelFactory(
            (activity?.application as RoomApplication).database.roomImagenDao()
        )
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("SIN CONEXION", roomViewModel.isNoticiasEmpty().toString())



        if(isOnline(requireContext())){
            lifecycleScope.launch(Dispatchers.IO) {
                addRoomNoticias()
                withContext(Dispatchers.Main){
                    cargarRoomNoticias()
                }
            }
        }
        else if(!roomViewModel.isNoticiasEmpty()){
            //CARGAR de ROOM
            Log.e("SIN CONEXION", roomViewModel.isNoticiasEmpty().toString())
            cargarRoomNoticias()
        }
        else{
            //NO ESTAS CONECTADO Y NO HAY DATOS
            binding.connectionStatus.text = "No tienes conexion, ni datos, conectese a Internet."
        }


    }

    private fun cargarRoomNoticias() {
        Log.e("cargarRoomNoticias", "ENTRO")
        val adapter = NoticiaListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToNoticiaDetailFragment(it._id)
            this.findNavController().navigate(action)
        }
        binding.noticiasGrid.adapter = adapter
        roomViewModel.allNoticias.observe(this.viewLifecycleOwner) { noticias ->
            noticias.let {
                adapter.submitList(it)
            }
        }
    }

    private suspend fun addRoomNoticias() {
        val listaNoticias = NewsAppApi.retrofitService.getNoticias()
        val listaImagenes = NewsAppApi.retrofitService.getImagenes()


        if (!roomViewModel.isNoticiasEmpty()){
            roomViewModel.borrarTablaNoticias()
            imagenesViewModel.borrarTabla()
        }

        for (noticia in listaNoticias) {
            roomViewModel.agregarNuevaNoticia(
                noticia._id,
                noticia.title,
                noticia.subtitle,
                noticia.category,
                noticia.body,
                noticia.author,
                noticia.approved,
                noticia.createdAt,
                noticia.updatedAt,
                noticia.__v
            )
        }

        for(imagen in listaImagenes){
            imagenesViewModel.agregarImagen(
                imagen._id,
                imagen.image,
                imagen.noticiaId,
                imagen.__v
            )
        }


    }

    fun isOnline(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivityManager != null){
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null){
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}