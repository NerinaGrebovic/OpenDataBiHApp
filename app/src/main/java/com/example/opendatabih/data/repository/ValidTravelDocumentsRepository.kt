package com.example.opendatabih.data.repository

import com.example.opendatabih.data.api.validtravel.ValidTravelDocumentsApi
import com.example.opendatabih.data.api.validtravel.ValidTravelDocumentsResponse
import com.example.opendatabih.data.local.dao.ValidTravelDocumentsDao
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity
import com.example.opendatabih.data.mapper.toEntityList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidTravelDocumentsRepository(
    private val api: ValidTravelDocumentsApi,
    private val dao: ValidTravelDocumentsDao
) {

    fun getValidTravelDocuments(): Flow<List<ValidTravelDocumentEntity>> = flow {

        val localData = dao.getAllDocuments()
        emit(localData)

        try {
            println(">>> Pokrećem API poziv za važeće putne isprave...")

            val request = com.example.opendatabih.data.model.ValidTravelDocumentsRequest(
                entityId = 0,
                cantonId = 0,
                municipalityId = 0,
                travelDocumentTypeId = 0
            )

            val response: ValidTravelDocumentsResponse = api.getValidTravelDocuments(request)

            println(">>> Broj rezultata: ${response.result.size}")
            response.result.forEach {
                println(" ${it.institution} — ${it.total}")
            }

            val documents = response.result.toEntityList()

            dao.deleteAll()
            dao.insertDocuments(documents)

            emit(dao.getAllDocuments())

        } catch (e: Exception) {
            println(">>> ERROR ValidTravelDocuments API: ${e.message}")
            emit(localData)
        }
    }
}
