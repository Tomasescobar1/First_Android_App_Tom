package com.example.tomkotlinsecondapp

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
    val firstName: String
)