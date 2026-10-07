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

@Entity(tableName = "guitarTable")
data class Specs (
    @PrimaryKey val guitarId: Int,
    @ColumnInfo(name = "color_name") val colorName: String,
    @ColumnInfo(name = "model_name") val modelName: String
)

@Dao
interface TomasSpecsDao {
    @Query("INSERT OR REPLACE INTO guitarTable (guitarId, color_name, model_name) VALUES (:guitarId, :colorName, :modelName)")
    suspend fun insertGuitarSpecs(guitarId: Int, colorName: String, modelName: String)

    @Query("SELECT * FROM guitarTable WHERE guitarId = :guitarId")
    suspend fun getTheGuitarSpecs(guitarId: Int) :Specs?
}

@Database (entities = [Specs::class], version = 1)
abstract class ImplementRoomDatabase : RoomDatabase() {
    abstract fun specsDao() : TomasSpecsDao
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

    val specsDAO = db.specsDao()

    var nameTest: String = ""

    fun insertParamsIntoRoom(guitarNumber: Int, colorName:String, modelName: String)
    {
        viewModelScope.launch {
            try
            {
                specsDAO.insertGuitarSpecs(guitarNumber, colorName, modelName)

                nameTest = specsDAO.getTheGuitarSpecs(1)?.modelName.toString()

                Log.d("insertUserIntoRoom", "'Guitar' added as: $nameTest")
            }
            catch(e: Exception)
            {
                Log.d("RoomVieWModel", "Couldn't insert specs into RoomDB :(")
            }
        }
    }

    fun getParamsFromRoom(guitarNumber: Int): String
    {
        var fetchedColor: String = ""

        viewModelScope.launch {
            try
            {
                fetchedColor = specsDAO.getTheGuitarSpecs(guitarNumber)?.modelName.toString()
            }
            catch(e: Exception)
            {
                Log.d("getParamsFromRoom", "Failed to fetch the params from the DB :(")
            }
        }

        return fetchedColor
    }
}