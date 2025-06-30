package com.example.opendatabih.presentation.screen.valid

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity
import com.example.opendatabih.data.repository.ValidTravelDocumentsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ValidTravelDocumentsViewModel(
    private val repository: ValidTravelDocumentsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _documents = MutableStateFlow<List<ValidTravelDocumentEntity>>(emptyList())
    val documents: StateFlow<List<ValidTravelDocumentEntity>> = _documents

    private val _filtered = MutableStateFlow<List<ValidTravelDocumentEntity>>(emptyList())
    val filtered: StateFlow<List<ValidTravelDocumentEntity>> = _filtered

    val sortOption = savedStateHandle.getStateFlow("sortOption", "Naziv (A-Z)")
    val selectedViewType = savedStateHandle.getStateFlow("selectedViewType", "Prikaži sve dokumente")
    val selectedCanton = savedStateHandle.getStateFlow("selectedCanton", "Svi kantoni")
    val selectedEntity = savedStateHandle.getStateFlow("selectedEntity", "Svi entiteti")
    val searchQuery = savedStateHandle.getStateFlow("searchQuery", "")

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        fetchValidDocuments()
    }

    fun fetchValidDocuments() {
        viewModelScope.launch {
            _isRefreshing.value = true

            repository.getValidTravelDocuments()
                .onStart { _isLoading.value = true }
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                    _isRefreshing.value = false
                }
                .collectLatest { result ->
                    _documents.value = result
                    applyFilterAndSort()
                    _isLoading.value = false
                    _isRefreshing.value = false
                    _error.value = null
                }
        }
    }

    fun updateSortOption(option: String) {
        savedStateHandle["sortOption"] = option
        applyFilterAndSort()
    }

    fun updateSelectedViewType(viewType: String) {
        savedStateHandle["selectedViewType"] = viewType
    }

    fun updateSelectedCanton(canton: String) {
        savedStateHandle["selectedCanton"] = canton
    }

    fun updateSelectedEntity(entity: String) {
        savedStateHandle["selectedEntity"] = entity
    }

    fun updateSearchQuery(query: String) {
        savedStateHandle["searchQuery"] = query
        applyFilterAndSort()
    }

    fun applyAllFilters() {
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val sort = sortOption.value
        val viewType = selectedViewType.value
        val canton = selectedCanton.value
        val entity = selectedEntity.value
        val query = searchQuery.value.trim().lowercase()

        val baseList = _documents.value.filter { doc ->
            val matchesCanton = if (canton == "Svi kantoni") true else doc.canton.trim().lowercase() == canton.trim().lowercase()
            val matchesEntity = if (entity == "Svi entiteti") true else doc.entity.trim().lowercase() == entity.trim().lowercase()

            val matchesCount = when (viewType) {
                "Prikaži samo preko 100 važećih" -> doc.total > 100
                "Prikaži samo manje od 100 važećih" -> doc.total < 100
                else -> true
            }

            val matchesSearch = doc.institution.trim().lowercase().contains(query)

            matchesCanton && matchesEntity && matchesCount && matchesSearch
        }

        val sortedList = baseList.sortedWith(
            when (sort) {
                "Naziv (A-Z)" -> compareBy { it.institution }
                "Naziv (Z-A)" -> compareByDescending { it.institution }
                "Najviše muških" -> compareByDescending { it.maleTotal }
                "Najviše ženskih" -> compareByDescending { it.femaleTotal }
                "Najviše ukupno" -> compareByDescending { it.total }
                else -> compareBy { it.institution }
            }
        )

        _filtered.value = sortedList
    }

    fun fetchDocuments() {
        fetchValidDocuments()
    }
}
