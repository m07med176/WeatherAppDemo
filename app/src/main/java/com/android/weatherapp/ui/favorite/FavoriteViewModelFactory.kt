package com.android.weatherapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.data.Repository
import com.android.weatherapp.data.RepositoryOperations

class FavoriteViewModelFactory(private val repository: RepositoryOperations):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(this.repository) as T
    }
}