package com.fcfm.newsapp

import android.os.Bundle
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
            Toast.makeText(context, "Logout!", Toast.LENGTH_SHORT).show()
            crearUsuario()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun crearUsuario(){
        val nuevoUsuario = NewUsuario("martin2", "garza2", "email2@gmail.com", "martin2", "martin2", arrayOf("usuario"))

        val call: Call<NewUsuario?>? = NewsAppApi.retrofitService.createUser(nuevoUsuario)

        call!!.enqueue(object: Callback<NewUsuario?>{
            override fun onResponse(call: Call<NewUsuario?>, response: Response<NewUsuario?>) {
                Toast.makeText(context, "DATA Added", Toast.LENGTH_SHORT).show()

                val response: NewUsuario? = response.body()

                val responseString = "Response Code : " + "201" + "\n" + "Name : " +  "Job : "
            }

            override fun onFailure(call: Call<NewUsuario?>, t: Throwable) {
                Toast.makeText(context, "FALLE", Toast.LENGTH_SHORT).show()
            }
        })
    }
}