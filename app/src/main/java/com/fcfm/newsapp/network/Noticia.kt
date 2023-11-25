package com.fcfm.newsapp.network

import com.fcfm.newsapp.model.Imagen

data class Noticia(
    val title: String,
    val subtitle: String,
    val category: String,
    val body: String,
    val author: String,
    val approved: Boolean
)

data class NoticiaFromAPI(
    val _id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val body: String,
    val author: String,
    val approved: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class RoomNoticia(
    val _id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val body: String,
    val author: String,
    val approved: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val imagen: Array<Imagen>
)
