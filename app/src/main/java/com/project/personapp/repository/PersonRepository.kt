package com.project.personapp.repository

import com.project.personapp.room.Person
import com.project.personapp.room.PersonDAO


//Repository: acts as bridge between the ViewModel and Data Source
class PersonRepository(private val personDAO: PersonDAO) {

    val contacts = personDAO.getAllContactsInDB()

    suspend fun insert(person: Person): Long{
        return personDAO.insertContact(person)
    }

    suspend fun update(person: Person){

        return personDAO.updateContact(
            person
        )
    }

    suspend fun delete(person: Person){
        return personDAO.deleteContact(
            person
        )
    }


    suspend fun deleteAll(){
        return personDAO.deleteAll()
    }


}