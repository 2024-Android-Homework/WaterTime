package com.wychlw.watertime.reminder

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun initNotification(ctx: Context) {
    val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "drink_water_compose_channel"
    val channelName = "Drink Water Compose Notification"

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Drink Water Compose Notification"
        }
        manager.createNotificationChannel(channel)
    }
}