package com.example.opendatabih.data.api

import com.example.opendatabih.data.api.lostdocuments.LostDocumentsApi
import com.example.opendatabih.data.api.validtravel.ValidTravelDocumentsApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDgxIiwibmJmIjoxNzUxMzg2MTk4LCJleHAiOjE3NTE0NzI1OTgsImlhdCI6MTc1MTM4NjE5OH0.070G9slD2egHQbwSbPUW3iu0WqIGi8Tq_KoTyX0xn72Y6rmWonNMQUBv8qNzUda2Dh7YfXfSgV6D-gVynkCoRggit pull origin main\n"
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