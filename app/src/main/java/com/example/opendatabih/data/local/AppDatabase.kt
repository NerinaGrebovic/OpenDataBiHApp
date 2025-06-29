package com.example.opendatabih.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.opendatabih.data.local.dao.FavoriteDao
import com.example.opendatabih.data.local.dao.ValidTravelDocumentsDao
import com.example.opendatabih.data.local.dao.LostDocumentsDao
import com.example.opendatabih.data.local.entity.FavoriteEntity
import com.example.opendatabih.data.local.entity.ValidTravelDocumentEntity
import com.example.opendatabih.data.local.entity.LostDocumentEntity

@Database(
    entities = [ValidTravelDocumentEntity::class, LostDocumentEntity::class, FavoriteEntity::class],
    version = 7
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun validTravelDocumentsDao(): ValidTravelDocumentsDao
    abstract fun lostDocumentsDao(): LostDocumentsDao
    abstract fun favoriteDao(): FavoriteDao
}

