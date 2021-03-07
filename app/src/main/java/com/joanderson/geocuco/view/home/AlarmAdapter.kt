package com.joanderson.geocuco.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.joanderson.geocuco.R
import com.joanderson.geocuco.data.model.GeoAlarm
import com.joanderson.geocuco.databinding.AdapterAlarmBinding

class AlarmAdapter(
        var alarms : ArrayList<GeoAlarm> = arrayListOf()
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding : AdapterAlarmBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_alarm,
                parent,
                false
        )
        return  AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bindView(alarms[position])
    }

    override fun getItemCount() = alarms.size

    class AlarmViewHolder(private val alarmBinding: AdapterAlarmBinding) : RecyclerView.ViewHolder(alarmBinding.root) {

        fun bindView(alarm: GeoAlarm) {
            alarmBinding.alarm = alarm
            alarmBinding.executePendingBindings()
        }

    }
}