package com.example.opendatabih.data.repository

import com.example.opendatabih.data.local.dao.FavoriteDao
import com.example.opendatabih.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {

    fun getAllFavorites(): Flow<List<FavoriteEntity>> = dao.getAllFavorites()

    suspend fun insertFavorite(favorite: FavoriteEntity) = dao.insertFavorite(favorite)

    suspend fun deleteFavorite(favorite: FavoriteEntity) = dao.deleteFavorite(favorite)
}
