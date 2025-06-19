package com.project.personapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.personapp.repository.PersonRepository
import com.project.personapp.room.Person
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: PersonRepository): ViewModel() {

    val users = repository.contacts
    private var isUpdateOrDelete = false
    private lateinit var personToUpdateOrDelete: Person

    val inputName = MutableLiveData<String?>()
    val inputSurname = MutableLiveData<String?>()
    val inputDepartment = MutableLiveData<String?>()
    val inputTime = MutableLiveData<String?>()
    val inputPhone = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun insert(person: Person) = viewModelScope.launch {
        repository.insert(person)
    }

    fun delete(person: Person) = viewModelScope.launch {
        repository.delete(person)
        inputName.value = null
        inputSurname.value = null
        inputDepartment.value= null
        inputTime.value= null
        inputPhone.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun update(person: Person) = viewModelScope.launch {
        repository.update(person)
        inputName.value = null
        inputSurname.value = null
        inputDepartment.value = null
        inputTime.value = null
        inputPhone.value =null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun saveOrUpdate() {
        if(isUpdateOrDelete){
            personToUpdateOrDelete.contact_name = inputName.value!!
            personToUpdateOrDelete.contact_surname = inputSurname.value!!
            personToUpdateOrDelete.contact_department = inputDepartment.value!!
            personToUpdateOrDelete.contact_time = inputTime.value!!
            personToUpdateOrDelete.contact_phone = inputPhone.value!!




            update(personToUpdateOrDelete)
        }else{
            val name = inputName.value!!
            val surname = inputSurname.value!!
            val department = inputDepartment.value!!
            val time = inputTime.value!!
            val phone = inputPhone.value!!
            insert(Person(0, name, surname, department, time, phone))
            inputName.value = null
            inputSurname.value = null
            inputDepartment.value =null
            inputTime.value = null
            inputPhone.value = null
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(personToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun initUpdateAndDelete(person: Person){
        inputName.value = person.contact_name
        inputSurname.value = person.contact_surname
        inputDepartment.value = person.contact_department
        inputTime.value = person.contact_time
        inputPhone.value = person.contact_phone
        isUpdateOrDelete = true
        personToUpdateOrDelete = person
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}