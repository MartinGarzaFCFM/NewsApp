package com.fcfm.newsapp.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.newsapp.data.RoomNoticia
import com.fcfm.newsapp.databinding.NoticiaViewItemBinding

class NoticiaListAdapter(private val onItemClicked: (RoomNoticia) -> Unit) :
    ListAdapter<RoomNoticia, NoticiaListAdapter.NoticiaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        return NoticiaViewHolder(NoticiaViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(noticia)
        }
        holder.bind(noticia)
    }

    class NoticiaViewHolder(private var binding:NoticiaViewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(noticia: RoomNoticia){
            binding.noticia = noticia

            //Convertir Imagen
            //binding.ivImagen.setImageBitmap(createBitmap(noticia.image))
            //binding.ivImagen.setImageResource(R.drawable)

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RoomNoticia>() {
        override fun areItemsTheSame(oldItem: RoomNoticia, newItem: RoomNoticia): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: RoomNoticia, newItem: RoomNoticia): Boolean {
            return oldItem.title == newItem.title
        }
    }
}

private fun createBitmap(base64: String): Bitmap {
    val imageBytes = Base64.decode(base64, 0)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}