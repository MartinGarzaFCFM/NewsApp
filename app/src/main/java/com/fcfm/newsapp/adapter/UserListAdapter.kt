package com.fcfm.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.newsapp.databinding.UserViewItemBinding
import com.fcfm.newsapp.network.Usuario

class UserListAdapter : ListAdapter<Usuario,
        UserListAdapter.UserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListAdapter.UserViewHolder {
        return UserViewHolder(UserViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UserListAdapter.UserViewHolder, position: Int) {
        val usuario = getItem(position)
        holder.bind(usuario)
    }

    class UserViewHolder(private var binding:UserViewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(Usuario: Usuario){
            binding.usuario = Usuario
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.username == newItem.username
        }
    }
}