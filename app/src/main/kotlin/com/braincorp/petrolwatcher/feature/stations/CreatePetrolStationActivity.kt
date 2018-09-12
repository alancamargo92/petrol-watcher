package com.braincorp.petrolwatcher.feature.stations

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.stations.adapter.FuelAdapter
import com.braincorp.petrolwatcher.feature.stations.contract.CreatePetrolStationActivityContract
import com.braincorp.petrolwatcher.feature.stations.map.OnCurrentLocationFoundListener
import com.braincorp.petrolwatcher.feature.stations.model.Fuel
import com.braincorp.petrolwatcher.feature.stations.model.PetrolStation
import com.braincorp.petrolwatcher.feature.stations.presenter.CreatePetrolStationActivityPresenter
import com.braincorp.petrolwatcher.ui.OnItemClickListener
import com.braincorp.petrolwatcher.utils.hasLocationPermission
import com.braincorp.petrolwatcher.utils.startFuelActivity
import com.braincorp.petrolwatcher.utils.startPetrolStationListActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_create_petrol_station.*
import kotlinx.android.synthetic.main.content_create_petrol_station.*

/**
 * The activity where petrol stations are created
 */
class CreatePetrolStationActivity : AppCompatActivity(),
        CreatePetrolStationActivityContract.View,
        View.OnClickListener,
        PlaceSelectionListener,
        OnCurrentLocationFoundListener,
        OnItemClickListener {

    private companion object {
        const val KEY_PETROL_STATION = "petrol_station"
        const val REQUEST_CODE_LOCATION = 1234
        const val REQUEST_CODE_FUEL = 5678
        const val TAG = "PETROL_WATCHER"
    }

    override lateinit var presenter: CreatePetrolStationActivityContract.Presenter

    private var petrolStation = PetrolStation()
    private lateinit var placeAutocompleteAddress: PlaceAutocompleteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_petrol_station)
        setupToolbar()
        bindPlaceAutocompleteFragment()
        presenter = CreatePetrolStationActivityPresenter(view = this)
        fab.setOnClickListener(this)
        bt_add_fuel.setOnClickListener(this)
        setupLocationButton()
        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_PETROL_STATION, petrolStation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsGranted = grantResults.all { it == PERMISSION_GRANTED }
        if (requestCode == REQUEST_CODE_LOCATION && permissionsGranted)
            enableLocationButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FUEL && resultCode == RESULT_OK) {
            val fuel = data!!.getParcelableExtra<Fuel>(FuelActivity.KEY_DATA)
            petrolStation.fuels.add(fuel)
            updateRecyclerView()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> save()
            R.id.bt_location -> useCurrentLocation()
            R.id.bt_add_fuel -> startFuelActivity(REQUEST_CODE_FUEL)
        }
    }

    /**
     * Function triggered when a RecyclerView item is clicked
     *
     * @param position the position in the list
     */
    override fun onItemClick(position: Int) {
        // TODO: implement
    }

    /**
     * Shows the petrol station list
     */
    override fun showPetrolStationList() {
        startPetrolStationListActivity(finishCurrent = true)
    }

    /**
     * Shows an invalid petrol station dialogue
     */
    override fun showInvalidPetrolStationDialogue() {
        AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.invalid_petrol_station_data)
                .setIcon(R.drawable.ic_error)
                .setNeutralButton(R.string.ok, null)
                .show()
    }

    override fun onPlaceSelected(place: Place?) {
        if (place?.address != null) {
            petrolStation.address = place.address.toString()
            petrolStation.latLng = place.latLng
        }
    }

    /**
     * Function to be triggered when the current address
     * is found
     *
     * @param address the address
     * @param latLng the latitude and longitude
     */
    override fun onCurrentLocationFound(address: String, latLng: LatLng) {
        placeAutocompleteAddress.setText(address)

        petrolStation.address = address
        petrolStation.latLng = latLng
    }

    override fun onError(error: Status?) {
        Log.e(TAG, error?.statusMessage)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun save() {
        petrolStation.name = edt_name.text.toString()
        presenter.savePetrolStation(petrolStation)
    }

    private fun bindPlaceAutocompleteFragment() {
        placeAutocompleteAddress = fragmentManager.findFragmentById(R.id.edt_address) as PlaceAutocompleteFragment
        placeAutocompleteAddress.setHint(getString(R.string.address))
        placeAutocompleteAddress.setOnPlaceSelectedListener(this)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            petrolStation = getParcelable(KEY_PETROL_STATION)
            placeAutocompleteAddress.setText(petrolStation.address)
        }
    }

    private fun setupLocationButton() {
        if (SDK_INT >= M) {
            if (hasLocationPermission())
                enableLocationButton()
            else
                requestPermissions(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        } else {
            enableLocationButton()
        }
    }

    private fun enableLocationButton() {
        bt_location.visibility = VISIBLE
        bt_location.setOnClickListener(this)
    }

    private fun useCurrentLocation() {
        presenter.getCurrentLocation(context = this, onCurrentLocationFoundListener =  this)
    }

    private fun updateRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val adapter = FuelAdapter(onItemClickListener = this, data = petrolStation.fuels)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
    }

}