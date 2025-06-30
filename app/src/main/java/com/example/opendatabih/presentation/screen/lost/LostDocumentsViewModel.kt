package com.example.opendatabih.presentation.screen.lost

import androidx.lifecycle.SavedStateHandle
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
    private val repository: LostDocumentsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _documents = MutableStateFlow<List<LostDocumentEntity>>(emptyList())
    val documents: StateFlow<List<LostDocumentEntity>> = _documents

    private val _filtered = MutableStateFlow<List<LostDocumentEntity>>(emptyList())
    val filtered: StateFlow<List<LostDocumentEntity>> = _filtered

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
        fetchLostDocuments()
    }

    fun fetchLostDocuments() {
        viewModelScope.launch {
            _isRefreshing.value = true
            println("REFRESH START") // log

            repository.getLostDocuments()
                .onStart { _isLoading.value = true }
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                    _isRefreshing.value = false
                    println("REFRESH ERROR: ${e.message}") // log
                }
                .collectLatest { result ->
                    _documents.value = result
                    applyFilterAndSort()
                    _isLoading.value = false
                    _isRefreshing.value = false
                    _error.value = null
                    println("REFRESH DONE") // log
                }
        }
    }

    fun updateSortOption(option: String) {
        savedStateHandle["sortOption"] = option
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
        applyAllFilters()
    }

    fun applyAllFilters() {
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val sort = sortOption.value
        val viewType = selectedViewType.value
        val canton = selectedCanton.value
        val entity = selectedEntity.value
        val searchQuery = searchQuery.value.trim().lowercase()

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
