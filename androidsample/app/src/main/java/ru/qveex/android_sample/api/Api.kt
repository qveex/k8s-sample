package ru.qveex.android_sample.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import kotlinx.serialization.json.Json
import ru.qveex.android_sample.models.User
import java.util.concurrent.TimeUnit

interface Api {

    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @POST("/users")
    suspend fun createUser(
        @Body user: User
    ): Response<Unit>

}


private val json = Json { ignoreUnknownKeys = true }
@OptIn(ExperimentalSerializationApi::class)
fun createApi() = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8080/")
    .client(
        OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    )
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()
    .create(Api::class.java)