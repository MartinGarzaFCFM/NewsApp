package com.fcfm.newsapp

import android.app.Activity
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
import com.fcfm.newsapp.databinding.FragmentRegisterBinding
import com.fcfm.newsapp.network.NewUsuario
import com.fcfm.newsapp.network.NewsAppApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //Image
    private var sImage: String? = ""

    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener {
            crearUsuario()
        }

        binding.uploadImageButton.setOnClickListener {
            chooseImage()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun crearUsuario(){

        val nuevoUsuario = NewUsuario(
            "${binding.nameInput.text}",
            "${binding.lastNameInput.text}",
            "${binding.emailInput.text}",
            "${binding.passwordInput.text}",
            "${binding.usernameInput.text}",
            "$sImage",
            "Usuario"
        )

        Log.e("USUARIONUEVO", nuevoUsuario.toString())

        val call: Call<NewUsuario?>? = NewsAppApi.retrofitService.createUser(nuevoUsuario)

        call!!.enqueue(object: Callback<NewUsuario?>{
            override fun onResponse(call: Call<NewUsuario?>, response: Response<NewUsuario?>) {
                Toast.makeText(context, "Usuario Agregado", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<NewUsuario?>, t: Throwable) {
                Toast.makeText(context, "Fallo el registro", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun arePasswordsTheSame(): Boolean{
        val password = binding.passwordInput.text
        val confirmPassword = binding.confirmPasswordInput.text
        Log.e("PASSWORD", "$password")
        Log.e("CONFIRMPASSWORD", "$confirmPassword")

        Log.e("contrase;as BOOL", "${password.equals(confirmPassword)}")

        return password.equals(confirmPassword)
    }

    private fun chooseImage(){
        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        activityResultLauncher.launch(myFileIntent)
    }
}