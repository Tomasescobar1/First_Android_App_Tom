package com.example.tomkotlinsecondapp

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Entity(tableName = "userTable")
data class User (
    @PrimaryKey val userId: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String
)

@Dao
interface TomasUserDao {
    @Query("SELECT * FROM userTable WHERE first_name LIKE :userName AND last_name LIKE :lastName")
    suspend fun getTheUserName(userName: String, lastName: String) :User?
}