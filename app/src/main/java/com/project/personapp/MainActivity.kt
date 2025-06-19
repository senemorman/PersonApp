package com.project.personapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.project.personapp.repository.PersonRepository
import com.project.personapp.room.PersonDatabase
import com.project.personapp.ui.theme.ContactAppTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database, repository, and viewmodel
        val database = PersonDatabase.getInstance(this)
        val repository = PersonRepository(database.personDAO)
        val viewModelFactory = PersonViewModelFactory(repository)
        val personViewModel = ViewModelProvider(this, viewModelFactory)[PersonViewModel::class.java]

        setContent {
            ContactAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactScreen(personViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit { putLong("last_open_time", System.currentTimeMillis()) }
    }
}