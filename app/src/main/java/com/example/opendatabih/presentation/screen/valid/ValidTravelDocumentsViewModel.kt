package com.example.opendatabih.presentation.screen.valid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity
import com.example.opendatabih.data.repository.ValidTravelDocumentsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ValidTravelDocumentsViewModel(
    private val repository: ValidTravelDocumentsRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<List<ValidTravelDocumentEntity>>(emptyList())
    val documents: StateFlow<List<ValidTravelDocumentEntity>> = _documents

    private val _filtered = MutableStateFlow<List<ValidTravelDocumentEntity>>(emptyList())
    val filtered: StateFlow<List<ValidTravelDocumentEntity>> = _filtered

    private val _sortOption = MutableStateFlow("Naziv (A-Z)")
    val sortOption: StateFlow<String> = _sortOption

    private val _selectedViewType = MutableStateFlow("Prikaži sve dokumente")
    val selectedViewType: StateFlow<String> = _selectedViewType

    private val _selectedCanton = MutableStateFlow("Svi kantoni")
    val selectedCanton: StateFlow<String> = _selectedCanton

    private val _selectedEntity = MutableStateFlow("Svi entiteti")
    val selectedEntity: StateFlow<String> = _selectedEntity

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchValidDocuments()
    }

    fun fetchValidDocuments() {
        viewModelScope.launch {
            repository.getValidTravelDocuments()
                .onStart { _isLoading.value = true }
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .collectLatest { result ->
                    _documents.value = result
                    applyFilterAndSort()
                    _isLoading.value = false
                    _error.value = null
                }
        }
    }

    fun updateSortOption(option: String) {
        _sortOption.value = option
        applyFilterAndSort()
    }

    fun updateSelectedViewType(viewType: String) {
        _selectedViewType.value = viewType
    }

    fun updateSelectedCanton(canton: String) {
        _selectedCanton.value = canton
    }

    fun updateSelectedEntity(entity: String) {
        _selectedEntity.value = entity
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilterAndSort()
    }

    fun applyAllFilters() {
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val sort = _sortOption.value
        val viewType = _selectedViewType.value
        val canton = _selectedCanton.value
        val entity = _selectedEntity.value
        val query = _searchQuery.value.trim().lowercase()

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
