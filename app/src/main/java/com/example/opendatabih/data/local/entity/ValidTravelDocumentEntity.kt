package com.example.opendatabih.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "valid_travel_documents")
data class ValidTravelDocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entity: String,
    val canton: String,
    val municipality: String,
    val institution: String,
    val updatedDate: String,
    val documentType: String,
    val maleTotal: Int,
    val femaleTotal: Int,
    val total: Int
)
