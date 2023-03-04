package com.android.weatherapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.local.Favorite
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: Repository):ViewModel() {

    private val _favoriteList = MutableLiveData<List<Favorite>>()
    val favoriteList:LiveData<List<Favorite>>
    get() = _favoriteList

    fun getFavoriteList(){
        viewModelScope.launch {
            _favoriteList.value = repository.getFavorites()
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