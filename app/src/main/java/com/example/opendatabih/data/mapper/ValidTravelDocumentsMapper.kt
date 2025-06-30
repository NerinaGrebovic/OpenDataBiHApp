package com.example.opendatabih.data.mapper

import ValidTravelDocument
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity

fun List<ValidTravelDocument>.toEntityList(): List<ValidTravelDocumentEntity> {
    return map {
        ValidTravelDocumentEntity(
            entity = it.entity ?: "Nepoznato",
            canton = it.canton ?: "Nepoznato",
            municipality = it.municipality ?: "Nepoznato",
            institution = it.institution,
            updatedDate= it.updatedDate?: "Nepoznato",
            documentType = it.documentType ?: "Nepoznato",
            maleTotal = it.maleTotal,
            femaleTotal = it.femaleTotal,
            total = it.total
        )
    }
}
