package com.fcfm.newsapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.fcfm.newsapp.data.RoomApplication
import com.fcfm.newsapp.data.RoomImagen
import com.fcfm.newsapp.data.RoomNoticia
import com.fcfm.newsapp.databinding.FragmentNoticiaDetailBinding
import com.fcfm.newsapp.viewModel.RoomImagenViewModel
import com.fcfm.newsapp.viewModel.RoomImagenViewModelFactory
import com.fcfm.newsapp.viewModel.RoomNoticiaViewModel
import com.fcfm.newsapp.viewModel.RoomNoticiaViewModelFactory

class NoticiaDetailFragment : Fragment() {
    lateinit var noticia: RoomNoticia

    private val navigationArgs: NoticiaDetailFragmentArgs by navArgs()

    private var _binding: FragmentNoticiaDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomNoticiaViewModel by activityViewModels {
        RoomNoticiaViewModelFactory(
            (activity?.application as RoomApplication).database.roomNoticiaDao()
        )
    }
    private val imageModel: RoomImagenViewModel by activityViewModels {
        RoomImagenViewModelFactory(
            (activity?.application as RoomApplication).database.roomImagenDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.noticiaId
        viewModel.cargarNoticia(id).observe(this.viewLifecycleOwner) {
            bind(it)
        }
        imageModel.cargar(id).observe(this.viewLifecycleOwner){
            bindImage(it)
        }
    }

    private fun bind(noticia: RoomNoticia){
        val bodyCharSequence: CharSequence = noticia.body
        binding.apply {
            title.text = noticia.title
            subtitle.text = noticia.subtitle
            category.text = noticia.category
            body.setText(bodyCharSequence)
        }
    }
    private fun bindImage(roomImagen: RoomImagen){
        val decodedString: ByteArray = Base64.decode(roomImagen.imagen, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.apply {
            binding.imagen.setImageBitmap(decodedByte)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticiaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}