package com.fcfm.newsapp.network

data class Noticia(
    val title: String,
    val subtitle: String,
    val category: String,
    val body: String,
    val image: String,
    val author: String,
    val approved: Boolean
)
