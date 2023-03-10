package com.android.weatherapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var db:RoomDB
    lateinit var dao: FavoriteDao
    @Before
    fun initDB() {
        // initialize database
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        ).
        allowMainThreadQueries().build()

        dao = db.favoriteDao()
    }

    @After
    fun close() {
        // close database
        db.close()
    }


    @Test
    fun getFavorites_insertFavoriteItems_countOfItemsSame() = runBlockingTest {
        // Given
        val data1 = Favorite(
            id = 1,
            weather = WeatherResponse()
        )
        dao.insertFavorite(data1)

        val data2 = Favorite(
            id = 2,
            weather = WeatherResponse()
        )
        dao.insertFavorite(data2)


        val data3 = Favorite(
            id = 3,
            weather = WeatherResponse()
        )
        dao.insertFavorite(data3)

        // When
        val results = dao.getFavorites().first()

        // Then
        MatcherAssert.assertThat(results.size, `is`(3))
    }


    @Test
    fun insertFavorite_insertSingleItem_returnItem() = runBlockingTest{
        // Given
        val data1 = Favorite(
            id = 1,
            weather = WeatherResponse()
        )
        // When
        dao.insertFavorite(data1)

        // Then
        val results = dao.getFavorites().first()
        MatcherAssert.assertThat(results[0], IsNull.notNullValue())

    }

    @Test
    fun deleteFavorite_deleteItem_checkIsNull() = runBlockingTest {
        // Given
        val data1 = Favorite(
            id = 1,
            weather = WeatherResponse()
        )
        dao.insertFavorite(data1)
        // When
        dao.deleteFavorite(data1)
        // Then
        val results = dao.getFavorites().first()
        assertThat(results, IsEmptyCollection.empty())
        assertThat(results.size,`is`(0))
    }
}