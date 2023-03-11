package com.android.weatherapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.android.weatherapp.MainCoroutineRule
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // FakeDataSource
    private var weatherResponse: WeatherResponse = WeatherResponse(
        current = null,
        lon = 52.2,
        lat = 45.0,
        timezone_offset = 55,
        timezone = "Africe/Cairo",
        hourly = emptyList(),
        daily = emptyList()
    )

    private var favoriteList: MutableList<Favorite> = mutableListOf<Favorite>(
        Favorite(id = 1, weather = weatherResponse),
        Favorite(id = 2, weather = weatherResponse),
        Favorite(id = 3, weather = weatherResponse),
        Favorite(id = 4, weather = weatherResponse),
    )

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository


    @Before
    fun initializeRepo(){
        remoteDataSource = FakeDataSource(favoriteList, weatherResponse)
        localDataSource = FakeDataSource(favoriteList, weatherResponse)
        repository = Repository(
            remoteDataSource,
            localDataSource
        )
    }
    @Test
    fun getFavorites_Nothing_resultOfFavoriteListIsSameSize() = mainCoroutineRule.runBlockingTest {
        // Given
        // When: request all  favorite list in room in repository
        val resutls = repository.getFavorites().first()

        // Then: size of favorite list will be same size 4
        assertThat(resutls.size,`is`(favoriteList.size))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertFavorite_insertItem_increaseSizeOfList() = mainCoroutineRule.runBlockingTest{
        // Given: item of favorite
        val item = Favorite(id = 5, weather = weatherResponse)

        // When: insert favorite in room in repository
        repository.insertFavorite(item)

        // Then: size of favorite list will be 5
        assertThat(favoriteList.size,`is`(5))
    }

    @Test
    fun deleteFavorite_deleteItem_decreaseSizeOfList() = mainCoroutineRule.runBlockingTest{
        // Given:  single item of favorite
        val item = favoriteList[0]

        // When: delete favorite in room in repository
        repository.deleteFavorite(item)

        // Then: size of favorite list will be 3
        assertThat(favoriteList.size,`is`(3))
    }

    @Test
    fun getWeatherDetails_Nothing_WeatherResponse() = mainCoroutineRule.runBlockingTest(){
        // Given
        // When: request weather details from retrofit in repository
        val results = repository.getWeatherDetails(
            latitude = 56.5,
            longitude = 54.5
        ).first()

        // Then: response is a same of fake WeatherResponse
        assertThat(results,`is`(weatherResponse))
    }
}