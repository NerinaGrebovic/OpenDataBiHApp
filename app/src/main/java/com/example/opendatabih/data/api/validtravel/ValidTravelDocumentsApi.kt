package com.example.opendatabih.data.api.validtravel

import com.example.opendatabih.data.model.ValidTravelDocumentsRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ValidTravelDocumentsApi {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json")
        @POST("api/ValidTravelDocuments/list")
        suspend fun getValidTravelDocuments(
            @Body request: ValidTravelDocumentsRequest
        ): ValidTravelDocumentsResponse


}
