package com.example.opendatabih.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.opendatabih.data.local.entity.LostDocumentEntity

@Dao
interface LostDocumentsDao {

    @Query("SELECT * FROM lost_documents")
    suspend fun getAllLostDocuments(): List<LostDocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLostDocuments(documents: List<LostDocumentEntity>)

    @Query("DELETE FROM lost_documents")
    suspend fun deleteAll()
}
