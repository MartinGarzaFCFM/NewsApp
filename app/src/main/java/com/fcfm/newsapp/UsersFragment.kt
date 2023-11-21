package com.fcfm.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fcfm.newsapp.adapter.UserListAdapter
import com.fcfm.newsapp.databinding.FragmentUsersBinding
import com.fcfm.newsapp.viewModel.UserViewModel

class UsersFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUsersBinding.inflate(inflater)
        //val binding = UserViewItemBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.usersGrid.adapter = UserListAdapter()

        return binding.root
    }
}