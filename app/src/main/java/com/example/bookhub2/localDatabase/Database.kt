package com.example.bookhub2.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[Entity1::class,Entity3::class],
    version =3,
)

abstract class Database:RoomDatabase() {
     abstract fun bookDao():Dao
     abstract fun proDao():ProfileDao
}