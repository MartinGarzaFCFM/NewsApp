package com.fcfm.newsapp.model

data class Imagen(
    val image: String,
    val noticiaId: String
)

data class ImagenFromAPI(
    val _id: String,
    val image: String,
    val noticiaId: String,
    val __v: Int
)
