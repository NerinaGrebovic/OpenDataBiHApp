package com.example.opendatabih.data.api.lostdocuments

data class LostDocumentInfo(
    val entity: String,
    val canton: String,
    val municipality: String,
    val institution: String,
    val updateDate: String,
    val total: Int
)
