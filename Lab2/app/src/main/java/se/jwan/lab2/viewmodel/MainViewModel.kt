package se.jwan.lab2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.jwan.lab2.data.repository.FirebaseRepository

open class MainViewModel(private val repository: FirebaseRepository) : ViewModel() {
    open val deviceState = repository.deviceState

    init {
        viewModelScope.launch {
            repository.fetchDeviceState()
        }
    }

    fun toggleSwitch(key: String, value: String) {
        repository.updateDeviceState(key, value)
    }
}
