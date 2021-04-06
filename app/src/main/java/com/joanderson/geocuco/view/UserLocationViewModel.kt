package com.joanderson.geocuco.view

import android.location.Location
import androidx.lifecycle.*

class UserLocationViewModel : ViewModel() {

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation : LiveData<Location> get() = _currentLocation

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun removeObserver(observer: Observer<in Location>) {
        currentLocation.removeObserver(observer)
    }

}