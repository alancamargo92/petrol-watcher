package com.braincorp.petrolwatcher.feature.stations.model

import android.os.Parcel
import android.os.Parcelable
import com.braincorp.petrolwatcher.database.Mappable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

/**
 * A petrol station
 *
 * @param name the name
 * @param address the address
 * @param city the city
 * @param country the country
 * @param latLng the latitude and longitude
 * @param locale the locale
 * @param fuels the fuels with their respective prices
 * @param rating the rating (from 1 to 5)
 * @param ratingCount the number of people who rated this station
 * @param ratingSum the sum of all the prices up to the current moment
 */
data class PetrolStation(var name: String = "",
                         var address: String = "",
                         var city: String = "",
                         var country: String = "",
                         var latLng: LatLng = LatLng(0.0, 0.0),
                         var locale: Locale = Locale.getDefault(),
                         var fuels: MutableSet<Fuel> = mutableSetOf(),
                         var rating: Int = 0,
                         var ratingCount: Int = 0,
                         var ratingSum: Int = 0) : Mappable, Parcelable {

    companion object CREATOR : Parcelable.Creator<PetrolStation> {
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_ADDRESS = "address"
        private const val KEY_CITY = "city"
        private const val KEY_COUNTRY = "country"
        private const val KEY_LAT = "lat"
        private const val KEY_LNG = "lng"
        private const val KEY_LOCALE = "locale"
        private const val KEY_FUEL_TYPES = "fuel_types"
        private const val KEY_FUEL_QUALITIES = "fuel_qualities"
        private const val KEY_FUEL_PRICES = "fuel_prices"
        private const val KEY_RATING = "rating"
        private const val KEY_RATING_COUNT = "rating_count"
        private const val KEY_RATING_SUM = "rating_sum"

        override fun createFromParcel(parcel: Parcel): PetrolStation {
            return PetrolStation(parcel)
        }

        override fun newArray(size: Int): Array<PetrolStation?> = arrayOfNulls(size)
    }

    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel): this() {
        with (parcel) {
            id = readString()!!
            name = readString()!!
            address = readString()!!
            city = readString()!!
            country = readString()!!
            val lat = readDouble()
            val lng = readDouble()
            latLng = LatLng(lat, lng)
            locale = Locale.forLanguageTag(readString())
            val types = readArray(javaClass.classLoader)!!
            val qualities = readArray(javaClass.classLoader)!!
            val prices = readArray(javaClass.classLoader)!!
            for (i in 0 until types.size) {
                val type = Fuel.Type.valueOf(types[i].toString())
                val quality = Fuel.Quality.valueOf(qualities[i].toString())
                val price = BigDecimal(prices[i].toString().toDouble())
                fuels.add(Fuel(type, quality, price))
            }
            rating = readInt()
            ratingCount = readInt()
            ratingSum = readInt()
        }
    }

    @Suppress("UNCHECKED_CAST")
    constructor(snapshot: DataSnapshot): this() {
        with (snapshot) {
            id = child(KEY_ID).value.toString()
            name = child(KEY_NAME).value.toString()
            address = child(KEY_ADDRESS).value.toString()
            city = child(KEY_CITY).value.toString()
            country = child(KEY_COUNTRY).value.toString()
            val lat = child(KEY_LAT).value.toString().toDouble()
            val lng = child(KEY_LNG).value.toString().toDouble()
            latLng = LatLng(lat, lng)
            locale = Locale.forLanguageTag(child(KEY_LOCALE).value.toString())
            val types = child(KEY_FUEL_TYPES).children.toList()
            val qualities = child(KEY_FUEL_QUALITIES).children.toList()
            val prices = child(KEY_FUEL_PRICES).children.toList()
            for (i in 0 until types.count()) {
                val type = Fuel.Type.valueOf(types[i].value.toString())
                val quality = Fuel.Quality.valueOf(qualities[i].value.toString())
                val price = BigDecimal(prices[i].value.toString())
                fuels.add(Fuel(type, quality, price))
            }
            rating = child(KEY_RATING).value.toString().toInt()
            ratingCount = child(KEY_RATING_COUNT).value.toString().toInt()
            ratingSum = child(KEY_RATING_SUM).value.toString().toInt()
        }
    }

    override var id: String = UUID.randomUUID().toString()

    /**
     * Converts the object to a map
     *
     * @return the map
     */
    override fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map[KEY_ID] = id
        map[KEY_NAME] = name
        map[KEY_ADDRESS] = address
        map[KEY_CITY] = city
        map[KEY_COUNTRY] = country
        map[KEY_LAT] = latLng.latitude
        map[KEY_LNG] = latLng.longitude
        map[KEY_LOCALE] = locale.toLanguageTag()
        map[KEY_FUEL_TYPES] = fuels.map { it.type.name }
        map[KEY_FUEL_QUALITIES] = fuels.map { it.quality.name }
        map[KEY_FUEL_PRICES] = fuels.map { it.price.toDouble() }
        map[KEY_RATING] = rating
        map[KEY_RATING_COUNT] = ratingCount
        map[KEY_RATING_SUM] = ratingSum
        return map
    }

    /**
     * Determines whether the data is valid
     *
     * @return true if positive, otherwise false
     */
    fun isValid(): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with (parcel) {
            writeString(id)
            writeString(name)
            writeString(address)
            writeString(city)
            writeString(country)
            writeDouble(latLng.latitude)
            writeDouble(latLng.longitude)
            writeString(locale.toLanguageTag())
            writeArray(fuels.map { it.type.name }.toTypedArray())
            writeArray(fuels.map { it.quality.name }.toTypedArray())
            writeArray(fuels.map { it.price.toDouble() }.toTypedArray())
            writeInt(rating)
            writeInt(ratingCount)
            writeInt(ratingSum)
        }
    }

    override fun describeContents(): Int = 0

}