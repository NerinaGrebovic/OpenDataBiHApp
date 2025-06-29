package com.example.opendatabih.data.local

import android.content.Context
import androidx.room.Room

object RoomInstance {

    lateinit var database: AppDatabase
        private set

    fun initDatabase(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "valid_travel_documents_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
