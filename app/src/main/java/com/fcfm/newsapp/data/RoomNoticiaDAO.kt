package com.fcfm.newsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomNoticiaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(roomNoticia: RoomNoticia)

    @Update
    suspend fun update(roomNoticia: RoomNoticia)

    @Delete
    suspend fun delete(roomNoticia: RoomNoticia)

    @Query("SELECT * from RoomNoticia WHERE _id = :id")
    fun getRoomNoticia(id: String): Flow<RoomNoticia>

    @Query("SELECT * from RoomNoticia ORDER BY title ASC")
    fun getNoticias(): Flow<List<RoomNoticia>>

    //Check if Empty
    @Query("SELECT (SELECT COUNT(*) FROM RoomNoticia) == 0")
    fun isEmpty(): Boolean

    @Query("DELETE FROM RoomNoticia")
    fun nukeTable();

    //@Query("SELECT * FROM RoomNoticia INNER JOIN RoomImagen ON RoomImagen.noticiaId = RoomNoticia._id")
    //fun getNoticiasConImagenes(): Flow<List<RoomNoticiaConImagen>>
}