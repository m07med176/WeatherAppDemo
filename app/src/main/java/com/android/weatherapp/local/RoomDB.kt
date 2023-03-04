package com.android.weatherapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoomDB:RoomDatabase() {
    abstract fun favoriteDao():FavoriteDao

    companion object {
        @Volatile
        private var instance: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                "weather"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}