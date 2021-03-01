package com.joanderson.geocuco.view.createalarm

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.joanderson.geocuco.R
import com.joanderson.geocuco.databinding.FragmentCreateAlarmBinding
import java.util.*

class CreateAlarmFragment : Fragment() {

    private var _binding: FragmentCreateAlarmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAlarmViewModel by activityViewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCreateAlarmBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.includeAlarmDetails.viewModel = viewModel
        binding.lifecycleOwner = this
        setOnClick()
        manageRepeatAlarm()
        manageSelectHoursOfDay()
        return view
    }

    private fun manageSelectHoursOfDay() {
        binding.includeAlarmDetails.btStartTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager, TimePickerFragment.START_TIME)
        }
        binding.includeAlarmDetails.btFinnishTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager, TimePickerFragment.FINNISH_TIME)
        }
    }

    private fun manageRepeatAlarm() {
        setDayPickerLanguageToPtBr()
    }

    private fun setDayPickerLanguageToPtBr() {
        binding.includeAlarmDetails.dayPicker.locale = Locale("pt", "BR")
    }

    private fun setOnClick() {
        binding.includeLocalSearch.btMapsSearchConfirmPlace.setOnClickListener {
            setSearchVisibility(false)
            setAlarmDetailsVisibility(true)
        }
        binding.includeAlarmDetails.miniMapLayout.setOnClickListener {
            setSearchVisibility(true)
            setAlarmDetailsVisibility(false)
        }
    }

    private fun setAlarmDetailsVisibility(isVisible: Boolean) {
        binding.includeAlarmDetails.root.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setSearchVisibility(isVisible: Boolean) {
        binding.includeLocalSearch.root.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}