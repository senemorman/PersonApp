package com.project.personapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons_table")
data class Person(

    @PrimaryKey(autoGenerate = true)
    val contact_id: Int,


    var contact_name: String,
    var contact_surname: String,
    var contact_department: String,
    var contact_time: String,
    var contact_phone: String
)