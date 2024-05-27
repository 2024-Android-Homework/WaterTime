package com.wychlw.watertime.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import com.wychlw.watertime.MainActivity
import com.wychlw.watertime.R
import kotlin.random.Random

class reminderReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "drink_water_compose_channel"

        val workerID = Random.nextInt()

        val notification = NotificationCompat.Builder(
            ctx, channelId
        ).setContentTitle("It's time to drink water!")
            .setContentText(quote.getRandomQuotes())
            .setSmallIcon(R.drawable.ic_water)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    ctx,
                    0,
                    Intent(ctx, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
        manager.notify(
            workerID,
            notification
        )

    }
}