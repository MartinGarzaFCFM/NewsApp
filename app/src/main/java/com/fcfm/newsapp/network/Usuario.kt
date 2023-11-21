package com.fcfm.newsapp.network

data class Usuario(
    val _id: String,
    val names: String,
    val lastNames: String,
    val email: String,
    val password: String,
    val username: String,
    val roles: Array<String>,
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
    val roles: Array<String>
)
