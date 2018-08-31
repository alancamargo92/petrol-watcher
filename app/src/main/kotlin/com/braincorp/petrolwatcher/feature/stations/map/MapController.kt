package com.braincorp.petrolwatcher.feature.stations.map

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import com.google.android.gms.maps.OnMapReadyCallback

/**
 * A map controller
 */
interface MapController {

    /**
     * Starts a map
     *
     * @param fragmentManager the fragment manager
     * @param mapId the ID of the map
     * @param onMapReadyCallback the callback to be triggered
     *                           once the map is ready
     */
    fun startMap(fragmentManager: FragmentManager,
                 @IdRes mapId: Int,
                 onMapReadyCallback: OnMapReadyCallback)

}