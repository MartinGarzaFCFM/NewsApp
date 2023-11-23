package com.fcfm.newsapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fcfm.newsapp.MainActivity.Companion.userProfile
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.databinding.FragmentUserProfileBinding
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.network.UpdateUsuario
import com.fcfm.newsapp.network.Usuario
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class UserProfileFragment : Fragment() {
    private lateinit var settingsDataStore: SettingsDataStore
    private var isUserLoggedIn = false

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private var sImage: String? = userProfile?.image.toString()



    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){ result:ActivityResult ->
        if(result.resultCode == RESULT_OK){
            val uri = result.data!!.data
            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                binding.ivProfilePic.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(context, "Imagen Seleccionada", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception){
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializar LoggedInDataStore
        settingsDataStore = SettingsDataStore(requireContext())

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        binding.uploadImageButton.setOnClickListener {
            chooseImage()
        }

        binding.saveButton.setOnClickListener {
            saveUser()
        }

        //Cargar Datos a la pantalla de el usuario Logeado
        val decodedString: ByteArray = Base64.decode(userProfile?.image.toString(), Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.ivProfilePic.setImageBitmap(decodedByte)

        loadDataOnScreen()

        return binding.root
    }

    private fun loadDataOnScreen() {
        var cs: CharSequence = userProfile?.names.toString()
        binding.namesInput.setText(cs)
        cs = userProfile?.lastNames.toString()
        binding.lastnamesInput.setText(cs)
        cs = userProfile?.email.toString()
        binding.emailInput.setText(cs)
        cs = userProfile?.username.toString()
        binding.usernameInput.setText(cs)
    }

    private fun saveUser() {
        //Get ID
        Log.d("userProfile COMPANION", userProfile.toString())

        val user = UpdateUsuario(
            userProfile?.ID.toString(),
            "${binding.namesInput.text}",
            "${binding.lastnamesInput.text}",
            "${binding.emailInput.text}",
            "${binding.usernameInput.text}",
            "${binding.usernamePasswordInput.text}",
            sImage.toString()
        )

        Log.e("USUARIONUEVO", user.toString())

        val call: Call<Usuario?>? = NewsAppApi.retrofitService.updateUser(user)

        call!!.enqueue(object: Callback<Usuario?> {
            override fun onResponse(call: Call<Usuario?>, response: Response<Usuario?>) {
                Toast.makeText(context, "Usuario Actualizado", Toast.LENGTH_SHORT).show()

                Log.e("RESPONSEFROM API", response.body().toString())

                val response: Usuario? = response.body()

                lifecycleScope.launch {
                    if (response != null) {
                        Log.d("LOGIN", "${response.username}")
                        settingsDataStore.saveUserToPreferencesStore(
                            true,
                            response._id,
                            response.names,
                            response.lastNames,
                            response.email,
                            response.username,
                            response.password,
                            response.image,
                            response.role,
                            requireContext()
                        )
                    }
                }

            }

            override fun onFailure(call: Call<Usuario?>, t: Throwable) {
                Toast.makeText(context, "Fallo el registro", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun chooseImage(){
        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        activityResultLauncher.launch(myFileIntent)
    }
}