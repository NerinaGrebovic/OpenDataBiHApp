package com.example.opendatabih.data.api.lostdocuments

data class LostDocumentRequest(
    val updateDate: String = "2025-06-03",
    val entityId: Int = 0,
    val cantonId: Int = 0,
    val municipalityId: Int = 0,
    val travelDocumentTypeId: Int = 0
)
