package com.joanderson.geocuco.view.createalarm

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.joanderson.geocuco.R
import com.joanderson.geocuco.databinding.FragmentCreateAlarmBinding
import com.joanderson.geocuco.observeOnce
import com.joanderson.geocuco.view.UserLocationViewModel
import java.util.*


class CreateAlarmFragment : Fragment() {

    private var locationPermissionGranted = false

    private var _binding: FragmentCreateAlarmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAlarmViewModel by activityViewModels()
    private val userLocationViewModel : UserLocationViewModel by activityViewModels()

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //it is not permitted
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle(getString(R.string.location_required))
                        setMessage(getString(R.string.location_required_message))
                        setPositiveButton(getString(R.string.allow)) { _, _ ->
                            getLocationPermission()
                        }
                        setNegativeButton(getString(R.string.close_app)) { _, _ ->
                            ActivityCompat.finishAffinity(requireActivity())
                        }
                    }.create().show()
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCreateAlarmBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.includeAlarmDetails.viewModel = viewModel
        binding.lifecycleOwner = this

        userLocationViewModel.currentLocation.observeOnce(viewLifecycleOwner, {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync { googleMap ->
                val locationToShow = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions().position(locationToShow).title("user's last location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationToShow))
            }
        })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}