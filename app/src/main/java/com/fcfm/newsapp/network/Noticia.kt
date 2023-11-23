package com.fcfm.newsapp.network

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
