package com.fcfm.newsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fcfm.newsapp.databinding.FragmentRegisterBinding
import com.fcfm.newsapp.network.NewUsuario
import com.fcfm.newsapp.network.NewsAppApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener {
            crearUsuario()
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
            arrayOf("Usuario"))

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
}