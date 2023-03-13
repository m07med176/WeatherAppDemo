package com.android.weatherapp.ui.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// TODO 1#8 Create ViewModel of Alert & ViewModelFactory
class AlertViewModelFactory constructor(private val dao:AlertDao /* You Should Pass Repository */ ) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(this.dao) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
