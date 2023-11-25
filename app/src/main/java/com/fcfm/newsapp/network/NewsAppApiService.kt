package com.fcfm.newsapp.network

import com.fcfm.newsapp.model.Imagen
import com.fcfm.newsapp.model.ImagenFromAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://newsapp-api.onrender.com" //CLOUD onRender

//private const val BASE_URL = "http://192.168.0.207:3500" //Main PC
//private const val BASE_URL = "http://148.234.36.197:3500" //Mi laptop
//private const val BASE_URL = "http://148.234.36.249:3500"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NewsAppApiService{
    @GET("users")
    suspend fun getUsers(): List<Usuario>

    @GET("users/{id}")
    fun getUsuario(@Path("id") id: String): Call<Usuario>

    @POST("users")
    fun createUser(@Body usuario: NewUsuario): Call<NewUsuario?>?

    @PATCH("users/updateSelf")
    fun updateUser(@Body user: UpdateUsuario): Call<Usuario?>?

    @DELETE("users/delete/{id}")
    fun borrarUsuario(@Path ("id") id: String): Call<Usuario?>?

    @POST("auth")
    fun login(@Body usuario: UsuarioForLogin): Call<Usuario?>?


    @GET("noticias")
    suspend fun getNoticias(): List<NoticiaFromAPI>

    @POST("noticias")
    fun createNoticia(@Body noticia: Noticia) : Call<NoticiaFromAPI>

    @POST("images")
    fun createImagen(@Body imagen: Imagen) : Call<ApiResponse>

    @GET("images")
    suspend fun getImagenes(): List<ImagenFromAPI>


}

object NewsAppApi{
    val retrofitService: NewsAppApiService by lazy {
        retrofit.create(NewsAppApiService::class.java)
    }
}