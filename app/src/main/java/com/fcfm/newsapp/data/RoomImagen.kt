package com.fcfm.newsapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomImagen(
    @PrimaryKey
    val _id: String,
    @ColumnInfo
    val imagen: String,
    @ColumnInfo
    val noticiaId: String,
    @ColumnInfo
    val __v: Int
)