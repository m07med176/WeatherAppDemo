package com.android.weatherapp.ui.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// TODO 1#8 Create ViewModel of Alert & ViewModelFactory
class AlertViewModel(private val dao:AlertDao /* You Should to put Repository in your project*/):ViewModel() {

    private val _stateGetAlert = MutableStateFlow<List<AlertModel>>(emptyList())
    val stateGetAlert: StateFlow<List<AlertModel>>
    get() = _stateGetAlert


    private val _stateInsetAlert = MutableStateFlow<Long>(0)
    val stateInsetAlert: StateFlow<Long>
    get() = _stateInsetAlert

    private val _stateSingleAlert = MutableStateFlow<AlertModel>(AlertModel())
    val stateSingleAlert: StateFlow<AlertModel>
    get() = _stateSingleAlert




    fun getAlerts(){
        viewModelScope.launch {
            dao.getAlerts().collect{
                _stateGetAlert.value = it
            }
        }
    }

    fun insertAlert(alert: AlertModel){
        viewModelScope.launch {
            // After Insert Model get id
            val id = dao.insertAlert(alert)

            // Pass Id in state flow
            _stateInsetAlert.value = id
        }
    }

    fun deleteAlert(alert: AlertModel){
        viewModelScope.launch {
            // Pass ID of Alert Model
            dao.deleteAlert(alert.id?:-1)
        }
    }

    fun getAlert(id: Int){
        viewModelScope.launch {
            // Get Alert By ID
            val alertModel  = dao.getAlert(id)
            _stateSingleAlert.value = alertModel
        }
    }

}