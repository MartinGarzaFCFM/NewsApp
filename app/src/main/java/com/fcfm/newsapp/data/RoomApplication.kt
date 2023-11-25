package com.fcfm.newsapp.data

import android.app.Application

class RoomApplication : Application() {
    val database: RoomNoticiaDatabase by lazy { RoomNoticiaDatabase.getDatabase(this) }
}