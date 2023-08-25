package com.pailan.ec4.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.pailan.ec4.model.Api
import androidx.room.*

@Dao
interface ApiDao {

    @Query("SELECT * FROM api")
    fun getFavorites(): List<Api>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(api: Api)

    @Delete
    suspend fun removeFavorite(api: Api)
}