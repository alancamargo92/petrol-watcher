package com.braincorp.petrolwatcher.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.support.v4.app.NotificationCompat
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.prediction.model.Prediction
import com.braincorp.petrolwatcher.feature.stations.MapActivity
import java.util.*

private const val CHANNEL_ID = "notification_channel"

/**
 * Shows a notification for fuel price predictions
 *
 * @param predictions the predictions
 * @param locale the locale
 */
fun Context.showNotificationForPredictions(predictions: List<Prediction>,
                                           locale: Locale) {
    val requestCode = 123
    val intent = MapActivity.getIntentForPredictionDialogue(this,
                                                            predictions as ArrayList<Prediction>,
                                                            locale)
    val flags = 0
    val action = PendingIntent.getActivity(this, requestCode, intent, flags)

    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_message))
            .setStyle(NotificationCompat.BigTextStyle()
                              .bigText(getString(R.string.notification_message)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // TODO: set vibration pattern, lights and sound
            .setAutoCancel(true)
            .setContentIntent(action)

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    if (SDK_INT >= O) {
        val channel = NotificationChannel(CHANNEL_ID,
                                          "channel_name",
                                          NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = 123
    notificationManager.notify(notificationId, builder.build())
}