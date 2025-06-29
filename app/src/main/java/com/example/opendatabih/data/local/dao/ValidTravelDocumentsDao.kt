package com.example.opendatabih.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity

@Dao
interface ValidTravelDocumentsDao {

    @Query("SELECT * FROM valid_travel_documents")
    suspend fun getAllDocuments(): List<ValidTravelDocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<ValidTravelDocumentEntity>)

    @Query("DELETE FROM valid_travel_documents")
    suspend fun deleteAll()
}
