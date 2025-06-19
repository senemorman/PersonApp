package com.project.personapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonDAO {

    @Insert
    suspend fun insertContact(person: Person): Long

    @Update
    suspend fun updateContact(person: Person)

    @Delete
    suspend fun deleteContact(person: Person)

    @Query("DELETE FROM persons_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM persons_table")
    fun getAllContactsInDB(): LiveData<List<Person>>
}