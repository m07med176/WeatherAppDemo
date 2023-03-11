package com.android.weatherapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.RepositoryOperations
import com.android.weatherapp.data.local.Favorite
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: RepositoryOperations):ViewModel() {

    // State


    private val viewModelTest = MutableLiveData<String>()

    private val _favoriteList = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteList:StateFlow<List<Favorite>>
    get() = _favoriteList

    private val _errorCatch = MutableLiveData<String>()
    val errorCatch:LiveData<String>
    get() = _errorCatch

    private val _loading = MutableStateFlow<Boolean>(true)
    val loading:StateFlow<Boolean>
        get() = _loading


    fun getViewModelForTest():LiveData<String>{
        return viewModelTest
    }

    suspend fun insertViewModelTest(item:String){
        viewModelTest.postValue(item)
    }
    fun getFavoriteList(){
        viewModelScope.launch {
            repository.getFavorites()
                .catch {
                    _errorCatch.value = it.message
                }
                .collect{favoriteList->
                _favoriteList.value = favoriteList
            }

        }
    }

    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }

    fun insertFavorite(favorite: Favorite){
        viewModelScope.launch{
            repository.insertFavorite(favorite)
        }
    }


}