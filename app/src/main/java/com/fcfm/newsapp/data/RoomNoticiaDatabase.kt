package com.fcfm.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomNoticia::class, RoomImagen::class], version = 2, exportSchema = false)
abstract class RoomNoticiaDatabase: RoomDatabase() {
    abstract fun roomNoticiaDao(): RoomNoticiaDAO
    abstract fun roomImagenDao(): RoomImagenDAO

    companion object{
        @Volatile
        private var INSTANCE: RoomNoticiaDatabase? = null

        fun getDatabase(context: Context): RoomNoticiaDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomNoticiaDatabase::class.java,
                    "noticia_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}