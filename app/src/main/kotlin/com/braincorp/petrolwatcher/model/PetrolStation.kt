package com.braincorp.petrolwatcher.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.braincorp.petrolwatcher.utils.fuelSetToStringFloatMap
import com.braincorp.petrolwatcher.utils.stringFloatMapToFuelList
import com.braincorp.petrolwatcher.utils.stringToRating
import com.google.firebase.database.DataSnapshot
import java.util.*

data class PetrolStation(var id: String = UUID.randomUUID().toString(),
                         var owner: String = "",
                         var name: String = "",
                         var address: String = "",
                         var fuels: MutableSet<Fuel> = emptySet<Fuel>().toMutableSet(),
                         var rating: Rating = Rating.OK) : Parcelable {

    companion object CREATOR : Parcelable.Creator<PetrolStation> {
        private const val KEY_ID = "id"
        private const val KEY_OWNER = "owner"
        private const val KEY_NAME = "name"
        private const val KEY_ADDRESS = "address"
        private const val KEY_FUELS = "fuels"
        private const val KEY_RATING = "rating"

        override fun createFromParcel(source: Parcel): PetrolStation = PetrolStation(source)

        override fun newArray(size: Int): Array<PetrolStation?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel): this() {
        id = parcel.readString()
        owner = parcel.readString()
        name = parcel.readString()
        address = parcel.readString()
        @Suppress("UNCHECKED_CAST")
        val fuelList = parcel.readParcelableArray(javaClass.classLoader) as Array<Fuel>
        fuels = fuelList.toMutableSet()
        rating = parcel.readSerializable() as Rating
    }

    constructor(snapshot: DataSnapshot): this() {
        id = snapshot.child(KEY_ID).value.toString()
        owner = snapshot.child(KEY_OWNER).value.toString()
        name = snapshot.child(KEY_NAME).value.toString()
        address = snapshot.child(KEY_ADDRESS).value.toString()

        val fuelsSnapshot = snapshot.child(KEY_FUELS).value
        @Suppress("UNCHECKED_CAST")
        val fuelList = if (fuelsSnapshot != null) {
            stringFloatMapToFuelList(snapshot.child(KEY_FUELS).value as Map<String, Float>)
        } else emptyList<Fuel>()
        fuels = fuelList.toMutableSet()

        rating = stringToRating(snapshot.value.toString())
    }

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map[KEY_ID] = id
        map[KEY_OWNER] = owner
        map[KEY_NAME] = name
        map[KEY_ADDRESS] = address
        map[KEY_FUELS] = fuelSetToStringFloatMap(fuels)
        map[KEY_RATING] = rating
        return map
    }

    fun allFieldsAreValid(): Boolean {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(owner)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeParcelableArray(fuels.toTypedArray(), 0)
        parcel.writeSerializable(rating)
    }

    override fun describeContents(): Int = 0

}