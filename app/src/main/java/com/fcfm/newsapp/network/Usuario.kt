package com.fcfm.newsapp.network

data class Usuario(
    val _id: String,
    val names: String,
    val lastNames: String,
    val email: String,
    val password: String,
    val username: String,
    val image: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
)

data class NewUsuario(
    val names: String,
    val lastNames: String,
    val email: String,
    val password: String,
    val username: String,
    val image: String,
    val role: String
)

data class UpdateUsuario(
    val id: String,
    val names: String,
    val lastNames: String,
    val email: String,
    val username: String,
    val password: String,
    val image: String
)

data class UserProfile(
    val loggedIn: Boolean,
    val ID: String,
    val names: String,
    val lastNames: String,
    val email: String,
    val username: String,
    val password: String,
    val image: String,
    val role: String
)

data class UsuarioForLogin(
    val username: String,
    val password: String
)
