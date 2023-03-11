package com.android.weatherapp.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.weatherapp.MainCoroutineRule
import com.android.weatherapp.data.FakeRepository
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.RepositoryOperations
import com.android.weatherapp.data.local.Favorite
import com.android.weatherapp.data.models.WeatherResponse
import com.android.weatherapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

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

    private lateinit var repository: RepositoryOperations
    private lateinit var favoriteViewModel: FavoriteViewModel

    @Before
    fun initTest(){
        repository = FakeRepository(favoriteList, weatherResponse)
        favoriteViewModel = FavoriteViewModel(repository)

    }
    @Test
    fun getFavoriteList_getItems_sameSizeOfFakeList() = mainCoroutineRule.runBlockingTest{
        // Given
        // When request list of favorite
        favoriteViewModel.getFavoriteList()

        // Then data of sate flow is same of list of fake data
        val data = favoriteViewModel.favoriteList.first()

        assertThat(data.size,`is`(4))
    }


    @Test
    fun deleteFavorite_deleteItem_DecreaseSizeOfFakeFavoriteList() {
        // Given item of favorite list
        val favoriteItem = favoriteList[0]

        // When delete favorite item in viewModel
        favoriteViewModel.deleteFavorite(favoriteItem)

        // Then: data of  fake favorite list  decrease in size than fake data
        assertThat(favoriteList.size,`is`(3))
    }

    @Test
    fun insertFavorite_insertItem_IncreaseSizeOfFakeFavoriteList() = mainCoroutineRule.runBlockingTest{
        // Given item of favorite list
        val favoriteItem = Favorite(9, WeatherResponse())

        // When insert favorite item in viewModel
        favoriteViewModel.insertFavorite(favoriteItem)

        // Then: data of fake favorite list  increase in size than fake data
        assertThat(favoriteList.size,`is`(5))
    }


    @Test
    fun testViewModel() = mainCoroutineRule.runBlockingTest{

        favoriteViewModel.insertViewModelTest("Mohamed")

        val data  = favoriteViewModel.getViewModelForTest().getOrAwaitValue()

        // Then: data of  fake favorite list  decrease in size than fake data
        assertThat(data,`is`("Mohamed"))
    }
}