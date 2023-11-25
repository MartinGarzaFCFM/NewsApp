package com.fcfm.newsapp

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fcfm.newsapp.adapter.UserListAdapter
import com.fcfm.newsapp.data.RoomNoticia
import com.fcfm.newsapp.network.Usuario
import com.fcfm.newsapp.viewModel.ApiStatus

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.baseline_broken_image_24)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<Usuario>?) {
    val adapter = recyclerView.adapter as UserListAdapter
    adapter.submitList(data)
}

@BindingAdapter("noticiasData")
fun bindNoticiasRecyclerView(recyclerView: RecyclerView,
                     data: List<RoomNoticia>?) {
    //val adapter = recyclerView.adapter as NoticiaListAdapter
    //adapter.submitList(data)
}

@BindingAdapter("ApiStatus")
fun bindStatus(statusImageView: ImageView,
               status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.baseline_error_24)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }

        else -> {}
    }
}

class BindingAdapters {
}