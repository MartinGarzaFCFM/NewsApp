package com.fcfm.newsapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomNoticia(
    @PrimaryKey
    val _id: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val subtitle: String,
    @ColumnInfo
    val category: String,
    @ColumnInfo
    val body: String,
    @ColumnInfo
    val author: String,
    @ColumnInfo
    val approved: Boolean,
    @ColumnInfo
    val createdAt: String,
    @ColumnInfo
    val updatedAt: String,
    @ColumnInfo
    val __v: Int
)

@Entity
data class RoomNoticiaConImagen(
    @PrimaryKey
    val _id: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val subtitle: String,
    @ColumnInfo
    val category: String,
    @ColumnInfo
    val body: String,
    @ColumnInfo
    val author: String,
    @ColumnInfo
    val approved: Boolean,
    @ColumnInfo
    val createdAt: String,
    @ColumnInfo
    val updatedAt: String,
    @ColumnInfo
    val __v: Int,
    @ColumnInfo
    val image: String
)