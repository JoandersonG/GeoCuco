package com.joanderson.geocuco.view.createalarm

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joanderson.geocuco.util.ObservableViewModel

class CreateAlarmViewModel : ObservableViewModel() {

    private val  _startTime = MutableLiveData("17h00")
    val startTime: LiveData<String> get() = _startTime

    private val _finnishTime = MutableLiveData("20h00")
    val finnishTime: LiveData<String> get() = _finnishTime

    private val _alarmName = MutableLiveData<String>()
    var alarmName : String?
        @Bindable
        get() = _alarmName.value
        set(value) {_alarmName.value = value}

    private val _repeatAlarm = MutableLiveData<Boolean>()
    val repeatAlarmLiveData : LiveData<Boolean> get() = _repeatAlarm
    var repeatAlarm : Boolean?
        @Bindable
        get() = _repeatAlarm.value
        set(value) {_repeatAlarm.value = value}

    private val _showTimeInterval = MutableLiveData<Boolean>()
    val showTimeIntervalLiveData : LiveData<Boolean> get() = _showTimeInterval
    var showTimeInterval : Boolean?
        @Bindable
        get() = _showTimeInterval.value
        set(value) {_showTimeInterval.value = value}


    fun setStartTime(hour: String) {
        _startTime.value = hour
    }

    fun setFinnishTime(hour: String) {
        _finnishTime.value = hour
    }

}