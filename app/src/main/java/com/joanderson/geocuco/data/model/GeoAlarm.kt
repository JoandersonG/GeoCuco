package com.joanderson.geocuco.data.model

class GeoAlarm(
        val id: Int,
        val address: String,
        val alarmIsOn: Boolean,
        val daysRepeat: List<String>?,
        val timeIntervalStart: String?,
        val timeIntervalFinnish: String?,
        val name: String?,
) {
        fun getFrequencyAndTimeInterval() : String {
                var result = "Ativo "
                daysRepeat?.forEach { result += "$it,"  }
                timeIntervalStart?.let {
                        result += " $it - $timeIntervalFinnish"
                }
                return if (result != "Ativo ") result else ""
        }
}