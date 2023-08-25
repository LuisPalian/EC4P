package com.pailan.ec4.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pailan.ec4.model.Api


@Database(entities = [Api::class], version = 1)
abstract class ApiDataBase: RoomDatabase() {
    abstract fun apiDao() : ApiDao

    companion object{
        @Volatile
        private var instance: ApiDataBase?= null
        fun getDatabase(contex: Context): ApiDataBase{
            if (instance==null){
                synchronized(this){
                    instance= buildDatabase(contex)
                }
            }
            return instance!!
        }
        private fun buildDatabase(contex: Context): ApiDataBase?{
            return Room.databaseBuilder(
                contex.applicationContext,
                ApiDataBase::class.java,
                "api_database"
            ).build()
        }
    }
}