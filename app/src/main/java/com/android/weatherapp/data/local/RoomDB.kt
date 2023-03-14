package com.android.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.weatherapp.ui.alert.AlertDao
import com.android.weatherapp.ui.alert.AlertModel
// TODO 1#3- Register Entity in room and upgrade level
@Database(entities = [HomeCash::class,AlertModel::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoomDB:RoomDatabase() {
    abstract fun homeCashDao(): HomeDao
    abstract fun alertDao(): AlertDao

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