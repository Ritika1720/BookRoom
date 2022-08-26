package com.example.bookhub2.localDatabase

import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Insert
    fun insertBook(bookEntity: Entity1)

    @Delete
    fun deleteBook(bookEntity: Entity1)

    @Query("SELECT * FROM favBook")
    fun getAllBooks():List<Entity1>

    @Query("SELECT * FROM favBook WHERE book_id=:bookId")
    fun getBookById(bookId:String):Entity1

}
@Dao
interface ProfileDao{
    @Insert
    fun insertProfile(proEntity:Entity3)
    @Update
    fun updateProfile(proEntity: Entity3)
    @Delete
    fun deleteProfile(proEntity: Entity3)

    @Query("SELECT * FROM profileInfo")
    fun getProfile():Entity3?

    @Query("SELECT id FROM profileInfo WHERE id=:id LIMIT 1")
    fun getInfoById(id:Int):Int?

}