package com.fcfm.newsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fcfm.newsapp.MainActivity.Companion.userProfile
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.databinding.FragmentCreateNewsBinding
import com.fcfm.newsapp.model.Imagen
import com.fcfm.newsapp.network.ApiResponse
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.network.Noticia
import com.fcfm.newsapp.network.NoticiaFromAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateNewsFragment : Fragment() {
    private lateinit var settingsDataStore: SettingsDataStore
    private var isUserLoggedIn = false

    private var _binding: FragmentCreateNewsBinding? = null
    private val binding get() = _binding!!

    //Image
    private var sImage: String? = ""
    private var listaImagenes: MutableList<String> = arrayListOf()

    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.e("RESULTDATA", result.data?.data.toString())
            if (result.data?.clipData != null) {
                val count = result.data!!.clipData!!.itemCount
                var currentItem: Int = 0
                binding.tvUploadedImages.text = "${count} imagenes para cargar"
                while (currentItem < count) {
                    val imageUri: Uri? = result.data!!.clipData?.getItemAt(currentItem)?.uri

                    try {
                        val inputStream =
                            imageUri?.let { requireContext().contentResolver.openInputStream(it) }
                        val myBitmap = BitmapFactory.decodeStream(inputStream)
                        val stream = ByteArrayOutputStream()
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val bytes = stream.toByteArray()
                        listaImagenes.add(Base64.encodeToString(bytes, Base64.DEFAULT).toString())
                        inputStream!!.close()

                    } catch (ex: Exception) {
                        Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    currentItem += 1
                }
                Toast.makeText(context, "${count} imagenes para cargar", Toast.LENGTH_SHORT).show()

            } else if (result.data?.data != null) {
                binding.tvUploadedImages.text = "1 imagen para cargar"
                val imageUri = result.data!!.data

                try {
                    val inputStream =
                        imageUri?.let { requireContext().contentResolver.openInputStream(it) }

                    val myBitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    listaImagenes.add(Base64.encodeToString(bytes, Base64.DEFAULT).toString())
                    inputStream!!.close()
                    Toast.makeText(context, "Imagen Seleccionada", Toast.LENGTH_SHORT).show()

                } catch (ex: Exception) {
                    Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNewsBinding.inflate(inflater, container, false)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categorias_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item))
            binding.categoryInput.adapter = adapter
        }


        binding.uploadButton.setOnClickListener {
            chooseImage()
        }

        binding.saveButton.setOnClickListener {
            if(guardarNoticia()){
                binding.idTVResponse.text = "Todo BIEN"
            }
            else{
                binding.idTVResponse.text = "ALgo salio mal"
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    private fun guardarNoticia(): Boolean {
        val title = binding.titleInput.text
        val subtitle = binding.subtitleInput.text
        val category = binding.categoryInput.selectedItem.toString()
        val body = binding.bodyInput.text
        val author = userProfile?.ID

        if (title.isEmpty() ||
            subtitle.isEmpty() ||
            category.isEmpty() ||
            body.isEmpty() ||
            author.isNullOrEmpty()
        ) {
            return false
        }

        var noticia = Noticia(title.toString(), subtitle.toString(), category, body.toString(), author, false)

        Log.d("NOTICIAMODEL", noticia.toString())

        val call: Call<NoticiaFromAPI> = NewsAppApi.retrofitService.createNoticia(noticia)

        call.enqueue(object: Callback<NoticiaFromAPI>{
            override fun onResponse(call: Call<NoticiaFromAPI>, response: Response<NoticiaFromAPI>) {
                val res = response.body()
                if(res != null){
                    for(imagen in listaImagenes){
                        guardarImagen(imagen, res._id)
                    }

                    findNavController().navigateUp()
                }
                else{
                    binding.idTVResponse.text = "ImagePostFail(162)"
                }
            }

            override fun onFailure(call: Call<NoticiaFromAPI>, t: Throwable) {
                Log.e("RESPUESTA DE API NOTICIA", t.message.toString())
            }

        })
        return true
    }

    fun guardarImagen(imagenBase64: String, noticiaId: String) {
        val nuevaImagen = Imagen(
            imagenBase64,
            noticiaId
        )

        val calL: Call<ApiResponse> = NewsAppApi.retrofitService.createImagen(nuevaImagen)

        calL.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                val res = response.body()
                if(res != null){
                    Log.e("API IMAGEN", res.message)

                    activity?.supportFragmentManager?.popBackStack()
                }
                else{
                    binding.idTVResponse.text = "ImagePostFail(162)"
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API IMAGEN", t.message.toString())
            }
        })
    }

    private fun chooseImage() {
        listaImagenes = arrayListOf()
        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        myFileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activityResultLauncher.launch(myFileIntent)


    }
}