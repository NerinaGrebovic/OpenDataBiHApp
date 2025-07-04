package com.example.opendatabih.presentation.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.opendatabih.data.api.RetrofitInstance
import com.example.opendatabih.data.local.RoomInstance
import com.example.opendatabih.data.repository.FavoriteRepository
import com.example.opendatabih.data.repository.LostDocumentsRepository
import com.example.opendatabih.data.repository.ValidTravelDocumentsRepository
import com.example.opendatabih.presentation.screen.favorite.FavoriteViewModel
import com.example.opendatabih.presentation.screen.favorite.FavoriteViewModelFactory
import com.example.opendatabih.presentation.screen.lost.LostDocumentsViewModel
import com.example.opendatabih.presentation.screen.lost.LostDocumentsViewModelFactory
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsViewModel
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsViewModelFactory

@Composable
fun provideLostDocumentsViewModel(navBackStackEntry: NavBackStackEntry): LostDocumentsViewModel {
    val api = RetrofitInstance.lostDocsApi
    val dao = RoomInstance.database.lostDocumentsDao()
    val repository = LostDocumentsRepository(api, dao)
    val factory = LostDocumentsViewModelFactory(repository, navBackStackEntry)
    return viewModel(navBackStackEntry, factory = factory)
}

@Composable
fun provideValidTravelDocumentsViewModel(navBackStackEntry: NavBackStackEntry): ValidTravelDocumentsViewModel {
    val api = RetrofitInstance.validTravelApi
    val dao = RoomInstance.database.validTravelDocumentsDao()
    val repository = ValidTravelDocumentsRepository(api, dao)
    val factory = ValidTravelDocumentsViewModelFactory(repository, navBackStackEntry)
    return viewModel(navBackStackEntry, factory = factory)
}

@Composable
fun provideFavoriteViewModel(): FavoriteViewModel {
    val dao = RoomInstance.database.favoriteDao()
    val repository = FavoriteRepository(dao)
    val factory = FavoriteViewModelFactory(repository)
    return viewModel(factory = factory)
}
