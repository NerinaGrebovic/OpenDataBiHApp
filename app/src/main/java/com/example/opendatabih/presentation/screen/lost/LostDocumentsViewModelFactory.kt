package com.example.opendatabih.presentation.screen.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.opendatabih.data.repository.LostDocumentsRepository
import com.example.opendatabih.presentation.screen.lost.LostDocumentsViewModel

class LostDocumentsViewModelFactory(
    private val repository: LostDocumentsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LostDocumentsViewModel::class.java)) {
            return LostDocumentsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}