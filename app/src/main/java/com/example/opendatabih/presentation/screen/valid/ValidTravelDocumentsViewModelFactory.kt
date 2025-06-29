package com.example.opendatabih.presentation.screen.valid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.opendatabih.data.repository.ValidTravelDocumentsRepository

class ValidTravelDocumentsViewModelFactory(
    private val repository: ValidTravelDocumentsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ValidTravelDocumentsViewModel::class.java)) {
            return ValidTravelDocumentsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}