package com.example.weather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.data.datastore.WeatherDataStore
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.listener.AppLocationListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var resolutionForResult: ActivityResultLauncher<IntentSenderRequest>? = null
    private lateinit var mBinding: FragmentSplashBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var dataStore: DataStore<Preferences>
    var isLocationAvailable = false

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var currentLocation: Location? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Location Callback
        createLocationCallBack()
        getActivityResult()
        checkAndEnableGps(requireActivity(), mBinding.root)
    }

    private fun getActivityResult() {
        resolutionForResult =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
                if (activityResult.resultCode == RESULT_OK) {
                    checkLocationPermission()
                } else {

                }
            }
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locResult: LocationResult) {
                super.onLocationResult(locResult)
                currentLocation = locResult.lastLocation
                if (currentLocation != null) {
                    lifecycleScope.launch {
                        WeatherDataStore.setLatitudeLongitude(
                            dataStore, " ${String.format("%.2f", currentLocation?.latitude)},${String.format("%.2f", currentLocation?.longitude)}"
                        )
                    }
                    mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                    navController.navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }

            override fun onLocationAvailability(loc: LocationAvailability) {
                super.onLocationAvailability(loc)
                isLocationAvailable = loc.isLocationAvailable
            }
        }
    }


    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun checkAndEnableGps(
        activity: Activity,
        view: View?
    ) {
        LocationServices.getSettingsClient(activity)
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(LocationRequest().apply {
                        LocationRequest.PRIORITY_HIGH_ACCURACY
                    }).build()
            )
            .addOnSuccessListener {
                //  GPS is already enable, callback GPS status through listener
                checkLocationPermission()
            }
            .addOnFailureListener { e ->
                // ask user GPS permission
                if (e is ResolvableApiException) {
                    val intentSenderRequest = IntentSenderRequest.Builder(e.resolution).build()
                    resolutionForResult?.launch(intentSenderRequest)
                } else {
                    showSnackBarWithEnableGPSCTA(activity, view)
                }
            }
    }


    private fun showSnackBarWithEnableGPSCTA(activity: Activity, view: View?) {
        if (view != null) {
            Snackbar.make(view, "something went wrong", Snackbar.LENGTH_SHORT).setAction(
                "retry"
            ) {
                checkAndEnableGps(activity, view)
            }.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            when {
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showSnackBarWithEnableGPSCTA(requireActivity(), mBinding.root)
                }

                foregroundPermissionApproved() -> {
                    getUserLocation()
                }

                // Location is  denied permanently
                !foregroundPermissionApproved() -> {
                    goToAppLocationSettings()
                }
            }
        }
    }

    private fun goToAppLocationSettings() {
        Snackbar.make(mBinding.root, "Allow location permission", Snackbar.LENGTH_LONG)
            .setAction("Allow") { gotoAppSettingsPermissions() }.show()
    }

    private fun gotoAppSettingsPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity?.packageName, null)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            val locationRequest = LocationRequest.create().apply {
                interval = UPDATE_INTERVAL
                fastestInterval = FASTEST_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            mFusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (e: Exception) {
            Log.d(TAG, "exception $e")
        }
    }


    private fun checkLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getUserLocation()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    companion object {
        private const val TAG = "SplashFragment"
        private const val UPDATE_INTERVAL = (10 * 1000).toLong()
        private const val FASTEST_INTERVAL: Long = 2000
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9999
    }

}