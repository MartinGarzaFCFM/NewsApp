package com.fcfm.newsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomImagenDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(roomImagen: RoomImagen)

    @Update
    suspend fun update(roomImagen: RoomImagen)

    @Delete
    suspend fun delete(roomImagen: RoomImagen)

    @Query("SELECT * from RoomImagen WHERE noticiaId = :id LIMIT 1")
    fun getRoomImagen(id: String): Flow<RoomImagen>

    @Query("SELECT * from RoomImagen")
    fun getImagenes(): Flow<List<RoomImagen>>

    //Check if Empty
    @Query("SELECT (SELECT COUNT(*) FROM RoomImagen) == 0")
    fun isEmpty(): Boolean

    @Query("DELETE FROM RoomImagen")
    fun nukeTable();
}