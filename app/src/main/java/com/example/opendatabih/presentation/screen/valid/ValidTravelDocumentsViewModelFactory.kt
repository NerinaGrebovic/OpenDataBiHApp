package com.example.opendatabih.presentation.screen.valid

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.opendatabih.data.repository.ValidTravelDocumentsRepository

class ValidTravelDocumentsViewModelFactory(
    private val repository: ValidTravelDocumentsRepository,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ValidTravelDocumentsViewModel::class.java)) {
            return ValidTravelDocumentsViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
