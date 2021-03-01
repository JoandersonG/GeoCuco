package com.joanderson.geocuco.view.createalarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import java.util.*


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: CreateAlarmViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        val twentyFourHourStyle = DateFormat.is24HourFormat(activity as Context)
        return TimePickerDialog(activity, this, hour, minute, twentyFourHourStyle)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        if (tag == START_TIME) {
            viewModel.setStartTime("${hourOfDay}h${if (minute < 10) "0$minute" else minute}")
        } else {
            viewModel.setFinnishTime("${hourOfDay}h${if (minute < 10) "0$minute" else minute}")
        }
    }

    companion object {
        const val START_TIME = "START_TIME"
        const val FINNISH_TIME = "FINNISH_TIME"
    }

}
