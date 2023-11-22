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
import com.fcfm.newsapp.MainActivity.Companion.userProfile
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.databinding.FragmentUserProfileBinding
import java.io.ByteArrayOutputStream


class UserProfileFragment : Fragment() {
    private lateinit var settingsDataStore: SettingsDataStore
    private var isUserLoggedIn = false

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private var sImage: String? = ""



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
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        binding.uploadImageButton.setOnClickListener {
            chooseImage()
        }

        binding.saveButton.setOnClickListener {
            saveUser()
        }

        val decodedString: ByteArray = Base64.decode(userProfile?.image.toString(), Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.ivProfilePic.setImageBitmap(decodedByte)

        return binding.root
    }

    private fun saveUser() {
        //Get ID
        Log.d("userProfile COMPANION", userProfile.toString())
    }

    private fun chooseImage(){
        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        activityResultLauncher.launch(myFileIntent)
    }
}