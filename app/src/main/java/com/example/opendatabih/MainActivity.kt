package com.example.opendatabih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.opendatabih.data.local.RoomInstance
import com.example.opendatabih.presentation.navigation.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RoomInstance.initDatabase(this)

        setContent {
            val navController = rememberNavController()
            MainScreen(navController = navController)
        }
    }
}
