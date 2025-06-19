package com.project.personapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.personapp.repository.PersonRepository

class PersonViewModelFactory(private val repository: PersonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}