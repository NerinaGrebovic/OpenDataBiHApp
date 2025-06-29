package com.example.opendatabih.data.api.lostdocuments
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LostDocumentsApi {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/LostTravelDocuments/list")
    suspend fun getLostDocuments(@Body request: LostDocumentRequest): LostDocumentResponse
}
