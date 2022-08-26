package com.example.bookhub2.localDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="favBookInfo")
data class Entity (
    @PrimaryKey val book_id:Int,
    @ColumnInfo(name="bookName") val book_name:String,
    @ColumnInfo(name="bookAuthor") val book_author:String,
    @ColumnInfo(name="bookPrice") val book_price:String,
    @ColumnInfo(name="bookRating") val book_rating:String,
    @ColumnInfo(name="bookImage") val book_img:String,
    @ColumnInfo(name="bookDesc") val book_desc:String,
        )
@Entity(tableName="favBook")
data class Entity1 (
    @PrimaryKey val book_id:String,
    @ColumnInfo(name="bookName") val book_name:String,
    @ColumnInfo(name="bookAuthor") val book_author:String,
    @ColumnInfo(name="bookPrice") val book_price:String,
    @ColumnInfo(name="bookRating") val book_rating:String,
    @ColumnInfo(name="bookImage") val book_img:String,
    @ColumnInfo(name="bookDesc") val book_desc:String,

)
@Entity(tableName = "profileInfo")
data class Entity3(
    @PrimaryKey var id:Int=0,
    @ColumnInfo(name="username")var username :String?,
    @ColumnInfo(name="imageURL")var imageURL:String?)