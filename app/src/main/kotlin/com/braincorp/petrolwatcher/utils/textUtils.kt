package com.braincorp.petrolwatcher.utils

import android.support.design.widget.TextInputEditText
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.*

/**
 * Shows a text field error
 *
 * @param field the text field
 * @param message the error message
 */
fun showFieldError(field: TextInputEditText, message: String) {
    field.error = message
    field.requestFocus()
}

/**
 * Sets a generic value to an EditText
 *
 * @param editText the EditText
 * @param value the value
 */
fun <T> setEditTextValue(editText: TextInputEditText, value: T) {
    value.let {
        if ((it is Int && it > 0) || (it is Float && it > 0f))
            editText.setText(it.toString())
        else if (it is String && it.isNotBlank())
            editText.setText(it)
    }
}

/**
 * Formats a price in the local currency
 *
 * @param price the price
 * @param locale the locale
 *
 * @return the price formatted in the local currency
 */
fun formatPriceAsCurrency(price: BigDecimal, locale: Locale): String {
    val roundedPrice = price.round(MathContext(3)) // precision = 3
    val formatter = DecimalFormat.getNumberInstance(Locale.GERMANY)
    with(formatter) {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formattedPrice = formatter.format(roundedPrice.toDouble())
    return "R$ $formattedPrice"
}

/**
 * Normalises an area
 *
 * @param city the city
 * @param country the country
 *
 * @return city and country, without spaces or
 * special characters, concatenated with an underscore
 */
fun normaliseArea(city: String, country: String): String {
    val countryTxt = if (country == "Brazil") "Brasil" else country
    var text = "${city.replace(" ", "").toLowerCase()}_${countryTxt.replace(" ", "").toLowerCase()}"
    text = Normalizer.normalize(text, Normalizer.Form.NFD)
    return text.replace("\\p{M}".toRegex(), "")
}
