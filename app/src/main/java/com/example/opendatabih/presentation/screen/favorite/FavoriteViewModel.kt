package com.example.opendatabih.presentation.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opendatabih.data.local.entity.FavoriteEntity
import com.example.opendatabih.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    val favorites = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    fun addToFavorites(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.insertFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }
    fun deleteFavoriteByInstitution(institution: String) {
        val favorite = favorites.value.find { it.institution == institution }
        if (favorite != null) {
            deleteFavorite(favorite)
        }
    }

}