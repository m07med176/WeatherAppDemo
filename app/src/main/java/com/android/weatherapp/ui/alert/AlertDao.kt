package com.android.weatherapp.ui.alert

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// TODO 1#2- Create DAO of Alert Entity
@Dao
interface AlertDao {

    @Query("SELECT * FROM alert")
    fun getAlerts(): Flow<List<AlertModel>>

    /**
     * - Should return Long to pic id of inserted row in db
     * @return Long
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertModel):Long

    @Query("DELETE FROM alert WHERE id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("SELECT * FROM alert WHERE id = :id")
    suspend fun getAlert(id: Int):AlertModel


}