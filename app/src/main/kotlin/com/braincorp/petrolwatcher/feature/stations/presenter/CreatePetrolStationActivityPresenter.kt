package com.braincorp.petrolwatcher.feature.stations.presenter

import android.content.Context
import com.braincorp.petrolwatcher.DependencyInjection
import com.braincorp.petrolwatcher.feature.stations.contract.CreatePetrolStationActivityContract
import com.braincorp.petrolwatcher.map.OnCurrentLocationFoundListener
import com.braincorp.petrolwatcher.feature.stations.model.PetrolStation
import com.google.android.gms.tasks.OnCompleteListener

/**
 * The implementation of the presentation layer
 * of the create petrol station activity
 */
class CreatePetrolStationActivityPresenter(private val view: CreatePetrolStationActivityContract.View)
    : CreatePetrolStationActivityContract.Presenter {

    /**
     * Saves a petrol station
     */
    override fun savePetrolStation(petrolStation: PetrolStation) {
        if (petrolStation.isValid()) {
            DependencyInjection.databaseManager.savePetrolStation(petrolStation, OnCompleteListener {
                view.showPetrolStationList()
            })
        } else {
            view.showInvalidPetrolStationDialogue()
        }
    }

    /**
     * Gets the current location
     *
     * @param context the Android context
     * @param onCurrentLocationFoundListener the callback to be triggered
     *                                       when the current location is found
     */
    override fun getCurrentLocation(context: Context,
                                    onCurrentLocationFoundListener: OnCurrentLocationFoundListener) {
        val mapController = DependencyInjection.mapController
        mapController.getCurrentLocation(context, onCurrentLocationFoundListener)
    }

}