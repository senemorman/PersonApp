package com.project.personapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class PersonDatabase: RoomDatabase() {

    //DAOs are responsible for defining methods that interact with the database.

    abstract val personDAO: PersonDAO

    //Singleton design pattern

    companion object{

        @Volatile
        private var INSTANCE: PersonDatabase? = null

        fun getInstance(context: Context): PersonDatabase {
            synchronized(this){

                var instance = INSTANCE

                if(instance == null){
                    //creating the database object
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PersonDatabase::class.java,
                        "persons_database"
                    ).build()
                }

                INSTANCE = instance
                return instance

            }
        }
    }
}