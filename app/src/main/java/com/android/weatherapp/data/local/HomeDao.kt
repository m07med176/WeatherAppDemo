package com.android.weatherapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {

    @Query("SELECT * FROM cash_table ORDER BY id DESC LIMIT 1")
    fun getHomeCash(): Flow<HomeCash>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeCash(homeCash: HomeCash)

    @Delete
    suspend fun deleteHomeCash(homeCash: HomeCash)

}