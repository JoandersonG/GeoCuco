package com.joanderson.geocuco.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.joanderson.geocuco.R
import com.joanderson.geocuco.data.model.GeoAlarm
import com.joanderson.geocuco.databinding.FragmentHomeBinding
import com.joanderson.geocuco.view.UserLocationViewModel

class HomeFragment : Fragment(), LocationListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationManager : LocationManager

    private val userLocationViewModel : UserLocationViewModel by activityViewModels()

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

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (isLocationPermitted()) {
            requestLocationUpdates()
        } else {
            getLocationPermission()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNewGeoAlarm.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateAlarmFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            10f,
            this
        )
    }

    /*Returns whether location is already permitted by user */
    private fun isLocationPermitted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationUpdates()
                    return
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        //it's set for user's phone to never ask again for location permission

                        val builder = AlertDialog.Builder(requireContext())
                        builder.apply {
                            setTitle(getString(R.string.location_required))
                            setMessage(getString(R.string.location_required_message))
                            setPositiveButton(getString(R.string.allow)) { _, _ ->
                            //send user to app settings
                                startInstalledAppDetailsActivity()

                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.location_app_settings_instruction),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            setNegativeButton(getString(R.string.close_app)) { _, _ ->
                            finishAffinity(requireActivity())
                            }
                        }.create().show()
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.apply {
                            setTitle(getString(R.string.location_required))
                            setMessage(getString(R.string.location_required_message))
                            setPositiveButton(getString(R.string.allow)) { _, _ ->
                                getLocationPermission()
                            }
                            setNegativeButton(getString(R.string.close_app)) { _, _ ->
                                finishAffinity(requireActivity())
                            }
                        }.create().show()
                    }
                }
            }
        }
    }

    private fun startInstalledAppDetailsActivity() {
        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.data = Uri.parse("package:" +  activity?.packageName)
        startActivityForResult(i, OPEN_LOCATION_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OPEN_LOCATION_PERMISSION) {
            getLocationPermission()
        }

    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        userLocationViewModel.updateCurrentLocation(location)
    }

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
        const val OPEN_LOCATION_PERMISSION = 10
    }
}