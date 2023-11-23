package com.fcfm.newsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.databinding.FragmentLoginBinding
import com.fcfm.newsapp.network.NewsAppApi
import com.fcfm.newsapp.network.Usuario
import com.fcfm.newsapp.network.UsuarioForLogin
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class LoginFragment : Fragment() {
    private lateinit var settingsDataStore: SettingsDataStore
    private var isUserLoggedIn = false

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            login()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Inicializar LoggedInDataStore
        settingsDataStore = SettingsDataStore(requireContext())

        settingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner, {  })
        settingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner, {value ->
            isUserLoggedIn = value
            avisarDeLogIn()
        })
    }

    private fun avisarDeLogIn() {
        if(isUserLoggedIn){
            binding.avisoLogIn.text = "Hay un inicio de sesion."
        }
    }

    private fun login() {
        val usuario = UsuarioForLogin(
            username = "${binding.usernameInput.text}",
            password = "${binding.passwordInput.text}"
        )

        val call: Call<Usuario?>? = NewsAppApi.retrofitService.login(usuario)

        call!!.enqueue(object: Callback<Usuario?>{
            override fun onResponse(call: Call<Usuario?>, response: retrofit2.Response<Usuario?>) {
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
                Log.e("LOGINFAILURE", "${t.message}")
            }
        })
    }
}