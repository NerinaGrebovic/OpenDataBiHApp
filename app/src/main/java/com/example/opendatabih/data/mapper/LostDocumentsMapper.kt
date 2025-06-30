package com.example.opendatabih.data.mapper

import com.example.opendatabih.data.api.lostdocuments.LostDocumentInfo
import com.example.opendatabih.data.local.entity.LostDocumentEntity

fun List<LostDocumentInfo>.toEntityList(): List<LostDocumentEntity> {
    return map {
        LostDocumentEntity(
            entity = it.entity,
            canton = it.canton,
            municipality = it.municipality,
            institution = it.institution,
            updateDate = it.updateDate,
            lostCount = it.total
        )
    }
}



