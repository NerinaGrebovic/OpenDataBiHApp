package com.example.opendatabih.data.api

import com.example.opendatabih.data.api.lostdocuments.LostDocumentsApi
import com.example.opendatabih.data.api.validtravel.ValidTravelDocumentsApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDgxIiwibmJmIjoxNzUxMjMzODE2LCJleHAiOjE3NTEzMjAyMTYsImlhdCI6MTc1MTIzMzgxNn0.oTZmpjORXjzGMYCW92N-Xn0pdZlnI96sNXOXPzK-rHPegCOA0w7cXbpWuItWeMYejpG5vBeki--z2QWFo25fzg"
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", TOKEN)
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val lostDocsApi: LostDocumentsApi by lazy {
        retrofit.create(LostDocumentsApi::class.java)
    }

    val validTravelApi: ValidTravelDocumentsApi by lazy {
        retrofit.create(ValidTravelDocumentsApi::class.java)
    }


}