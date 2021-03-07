package com.joanderson.geocuco.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joanderson.geocuco.data.model.GeoAlarm
import com.joanderson.geocuco.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val alarmAdapter = AlarmAdapter(arrayListOf(
                GeoAlarm(1,"Rua Santo Antonio, cond Village", true, listOf("SEG", "QUA", "SEX"), "21H00", "23h50", null),
                GeoAlarm(2,"Rua qualquer", false, listOf("SEX", "SAB"), null, null, "Estágio STI")
        ))
        binding.rvAlarmsOn.adapter = alarmAdapter
        binding.rvAlarmsOff.adapter = alarmAdapter
        return binding.root
    }
}