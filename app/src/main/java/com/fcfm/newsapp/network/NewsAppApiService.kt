package com.fcfm.newsapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//private const val BASE_URL = "http://192.168.0.207:3500" //Main PC
private const val BASE_URL = "http://148.234.36.197:3500" //Mi laptop

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

    @POST("users")
    fun createUser(@Body usuario: NewUsuario): Call<NewUsuario?>?

    @POST("auth")
    fun login(@Body usuario: UsuarioForLogin): Call<Usuario?>?


    @POST("noticias")
    fun createNoticia(@Body noticia: Noticia) : Call<String>
}

object NewsAppApi{
    val retrofitService: NewsAppApiService by lazy {
        retrofit.create(NewsAppApiService::class.java)
    }
}