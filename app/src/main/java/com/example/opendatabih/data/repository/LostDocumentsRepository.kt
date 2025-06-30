package com.example.opendatabih.data.repository

import com.example.opendatabih.data.api.lostdocuments.LostDocumentsApi
import com.example.opendatabih.data.api.lostdocuments.LostDocumentRequest
import com.example.opendatabih.data.local.dao.LostDocumentsDao
import com.example.opendatabih.data.local.entity.LostDocumentEntity
import com.example.opendatabih.data.mapper.toEntityList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LostDocumentsRepository(
    private val api: LostDocumentsApi,
    private val dao: LostDocumentsDao
) {

    fun getLostDocuments(): Flow<List<LostDocumentEntity>> = flow {


        val localData = dao.getAllLostDocuments()
        emit(localData)

        try {
            println(">>> Pokrećem API poziv za izgubljene dokumente...")

            val request = LostDocumentRequest()
            val response = api.getLostDocuments(request)

            println(">>> Response success: ${response.success}")
            println(">>> Broj rezultata: ${response.result.size}")

            val documents = response.result.toEntityList()

            dao.deleteAll()
            dao.insertLostDocuments(documents)

            emit(dao.getAllLostDocuments())

        } catch (e: Exception) {
            println(">>> ERROR LostDocuments API: ${e.message}")
            emit(localData)
        }
    }
}
