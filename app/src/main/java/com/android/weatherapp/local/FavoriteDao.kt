package com.android.weatherapp.local

import androidx.room.*

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_table")
    suspend fun getFavorites():List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}