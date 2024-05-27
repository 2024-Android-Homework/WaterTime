package com.wychlw.watertime.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wychlw.watertime.R
import kotlin.random.Random

class reminderWorker(
    val ctx: Context,
    val params: WorkerParameters):
        Worker(ctx, params) {
    override fun doWork(): Result {
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "drink_water_compose_channel"
        val channelName = "Drink Water Compose Notification"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val workerID = Random.nextInt()

        val notification = NotificationCompat.Builder(
            ctx, "drink_water_notification"
        ).setContentTitle("It's time to drink water!")
            .setContentText(quote.getRandomQuotes())
            .setSmallIcon(R.drawable.ic_water)
            .setAutoCancel(true)
            .build()
        manager.notify(
            workerID,
            notification
        )

        return Result.success()
    }
}