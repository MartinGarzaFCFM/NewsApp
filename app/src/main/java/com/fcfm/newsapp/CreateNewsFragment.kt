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
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.databinding.FragmentCreateNewsBinding
import com.fcfm.newsapp.model.Imagen
import com.fcfm.newsapp.network.NewsAppApi
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
            if (result.data?.clipData != null) {
                val count: Int? = result.data!!.clipData?.itemCount
                var currentItem: Int = 0
                while (currentItem < count!!) {
                    val imageUri: Uri? = result.data!!.clipData?.getItemAt(currentItem)?.uri


                    binding.tvUploadedImages.text = "${count} imagenes para cargar"

                    try {
                        val inputStream = imageUri?.let {requireContext().contentResolver.openInputStream(it)}
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

                for (imagen in listaImagenes) {
                    guardarImagen(imagen, "asdf")
                }

            } else if (result.data != null) {
                val imagePath = result.data.toString()
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
            guardarNoticia()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun guardarNoticia() {
        TODO("Not yet implemented")
    }

    fun guardarImagen(imagenBase64: String, noticiaId: String){
        val nuevaImagen = Imagen(
            imagenBase64,
            noticiaId
        )

        val calL: Call<Imagen?>? = NewsAppApi.retrofitService.createImagen(nuevaImagen)

        calL!!.enqueue(object: Callback<Imagen?> {
            override fun onResponse(call: Call<Imagen?>, response: Response<Imagen?>) {
                Log.d("RESPONSEFROM API", response.body().toString())
            }

            override fun onFailure(call: Call<Imagen?>, t: Throwable) {
                Toast.makeText(context, "Fallo el registro", Toast.LENGTH_SHORT).show()
                Log.e("RESPONSEFAIL API", t.toString())
            }

        })

    }

    private fun chooseImage() {
        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        myFileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activityResultLauncher.launch(myFileIntent)


    }
}