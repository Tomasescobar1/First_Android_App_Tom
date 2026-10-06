package com.example.tomkotlinsecondapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.Room
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Entity(tableName = "userTable")
data class User (
    @PrimaryKey val userId: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String
)

@Dao
interface TomasUserDao {
    @Query("INSERT OR REPLACE INTO userTable (userId, first_name, last_name) VALUES (:userId, :userName, :lastName)")
    suspend fun insertUserName(userId: Int, userName: String, lastName: String)

    @Query("SELECT * FROM userTable WHERE first_name LIKE :userName AND userId = :userId")
    suspend fun getTheUserName(userName: String, userId: Int) :User?
}

@Database (entities = [User::class], version = 1)
abstract class ImplementRoomDatabase : RoomDatabase() {
    abstract fun userDao() : TomasUserDao
}

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : ImplementRoomDatabase
    {
        return Room.databaseBuilder<ImplementRoomDatabase>(
            context = context,
            name = context.getDatabasePath("myAppDatabase.db").absolutePath
            )
            .setDriver(BundledSQLiteDriver())
            .build()
    }

}

@HiltViewModel
class RoomViewModel @Inject constructor(private val db: ImplementRoomDatabase) : ViewModel() {
    val userDAO = db.userDao()

    var nameTest: String = ""

    fun insertUserIntoRoom(userNumber: Int, userName:String, lastName: String)
    {
        viewModelScope.launch {
            try
            {
                userDAO.insertUserName(userNumber, userName, lastName)

                nameTest = userDAO.getTheUserName(userName, userNumber)?.lastName.toString()

                Log.d("insertUserIntoRoom", "'User' added as: $nameTest")
            }
            catch(e: Exception)
            {
                Log.d("RoomVieWModel", "Couldn't insert user into RoomDB :(")
            }
        }
    }
}