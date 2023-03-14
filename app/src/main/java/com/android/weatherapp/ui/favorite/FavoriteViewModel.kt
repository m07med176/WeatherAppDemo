package com.android.weatherapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.local.HomeCash
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: Repository):ViewModel() {

    // State

    private val _homeCashList = MutableStateFlow<HomeCash>(HomeCash())
    val homeCashList:StateFlow<HomeCash>
    get() = _homeCashList

    private val _errorCatch = MutableLiveData<String>()
    val errorCatch:LiveData<String>
    get() = _errorCatch

    private val _loading = MutableStateFlow<Boolean>(true)
    val loading:StateFlow<Boolean>
        get() = _loading


    fun getFavoriteList(){
        viewModelScope.launch {
            repository.getHomeCash()
                .catch {
                    _errorCatch.value = it.message
                }
                .collect{favoriteList->
                _homeCashList.value = favoriteList
            }

        }
    }

    fun deleteFavorite(homeCash: HomeCash){
        viewModelScope.launch {
            repository.deleteHomeCash(homeCash)
        }
    }

    fun insertFavorite(homeCash: HomeCash){
        viewModelScope.launch{
            repository.insertHomeCash(homeCash)
        }
    }


}