package com.braincorp.petrolwatcher.database

import com.braincorp.petrolwatcher.feature.vehicles.listeners.OnVehiclesFoundListener
import com.braincorp.petrolwatcher.feature.vehicles.model.Vehicle
import com.google.android.gms.tasks.OnCompleteListener

/**
 * A database manager
 */
interface DatabaseManager {
    /**
     * Fetches all vehicles belonging to the currently
     * signed in user
     *
     * @param onVehiclesFoundListener the listener to be triggered when the
     *                                query is complete
     */
    fun fetchVehicles(onVehiclesFoundListener: OnVehiclesFoundListener)

    /**
     * Deletes a vehicle
     *
     * @param vehicle the vehicle to delete
     */
    fun deleteVehicle(vehicle: Vehicle)

    /**
     * Saves a vehicle
     *
     * @param vehicle the vehicle to save
     * @param onCompleteListener the callback to be triggered when the
     *                           operation is complete
     */
    fun saveVehicle(vehicle: Vehicle, onCompleteListener: OnCompleteListener<Void>)
}