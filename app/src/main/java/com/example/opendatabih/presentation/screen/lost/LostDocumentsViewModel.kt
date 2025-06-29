package com.example.opendatabih.presentation.screen.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opendatabih.data.local.entity.LostDocumentEntity
import com.example.opendatabih.data.repository.LostDocumentsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LostDocumentsViewModel(
    private val repository: LostDocumentsRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<List<LostDocumentEntity>>(emptyList())
    val documents: StateFlow<List<LostDocumentEntity>> = _documents

    private val _filtered = MutableStateFlow<List<LostDocumentEntity>>(emptyList())
    val filtered: StateFlow<List<LostDocumentEntity>> = _filtered

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
        fetchLostDocuments()
    }

    fun fetchLostDocuments() {
        viewModelScope.launch {
            repository.getLostDocuments()
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
        applyAllFilters()
    }

    fun applyAllFilters() {
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val sort = _sortOption.value
        val viewType = _selectedViewType.value
        val canton = _selectedCanton.value
        val entity = _selectedEntity.value
        val searchQuery = _searchQuery.value.trim().lowercase()

        val baseList = _documents.value.filter { doc ->
            val matchesCanton = if (canton == "Svi kantoni") true else doc.canton?.trim()?.lowercase() == canton.trim().lowercase()
            val matchesEntity = if (entity == "Svi entiteti") true else doc.entity?.trim()?.lowercase() == entity.trim().lowercase()

            val matchesCount = when (viewType) {
                "Prikaži samo preko 100 izgubljenih" -> doc.lostCount > 100
                "Prikaži samo manje od 100 izgubljenih" -> doc.lostCount < 100
                else -> true
            }

            val matchesSearch = doc.institution.trim().lowercase().contains(searchQuery)

            matchesCanton && matchesEntity && matchesCount && matchesSearch
        }

        val sortedList = baseList.sortedWith(
            when (sort) {
                "Naziv (A-Z)" -> compareBy { it.institution }
                "Naziv (Z-A)" -> compareByDescending { it.institution }
                "Najmanje izgubljenih" -> compareBy { it.lostCount }
                "Najviše izgubljenih" -> compareByDescending { it.lostCount }
                else -> compareBy { it.institution }
            }
        )

        _filtered.value = sortedList
    }

    fun fetchDocuments() {
        fetchLostDocuments()
    }
}
