package com.example.opendatabih.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "lost_documents")
data class LostDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entity: String?,
    val canton: String?,
    val municipality: String?,
    val institution: String,
    val updateDate: String?,
    val lostCount: Int
)

