package com.wychlw.watertime.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wychlw.watertime.MainActivity
import com.wychlw.watertime.R
import kotlin.random.Random

class reminderWorker(
    val ctx: Context,
    val params: WorkerParameters):
        Worker(ctx, params) {
    override fun doWork(): Result {
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "drink_water_compose_channel"

        val workerID = Random.nextInt()

        val notification = NotificationCompat.Builder(
            ctx, channelId
        ).setContentTitle("It's time to drink water!")
            .setContentText(quote.getRandomQuotes())
            .setSmallIcon(R.drawable.ic_water)
            .setAutoCancel(true)
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

        return Result.success()
    }
}