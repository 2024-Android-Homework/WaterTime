package com.wychlw.watertime.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

private fun getAllRequestIDs(context: Context): List<UUID> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("WorkPrefs", Context.MODE_PRIVATE)
    val requestIDs = sharedPreferences.getStringSet("requestIDs", emptySet())?.map { UUID.fromString(it) } ?: emptyList()
    return requestIDs
}

private fun addRequestID(context: Context, requestCode: UUID) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("WorkPrefs", Context.MODE_PRIVATE)
    val requestIDs = sharedPreferences.getStringSet("requestIDs", emptySet())?.toMutableSet() ?: mutableSetOf()
    requestIDs.add(requestCode.toString())
    sharedPreferences.edit().putStringSet("requestIDs", requestIDs).apply()
}

private fun removeAllRequestIDs(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("WorkPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putStringSet("requestIDs", emptySet()).apply()
}

private fun removeRequestID(context: Context, requestCode: UUID) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("WorkPrefs", Context.MODE_PRIVATE)
    val requestIDs = sharedPreferences.getStringSet("requestIDs", emptySet())?.toMutableSet() ?: mutableSetOf()
    requestIDs.remove(requestCode.toString())
    sharedPreferences.edit().putStringSet("requestIDs", requestIDs).apply()
}

fun reminderPeriodicSchedule(ctx: Context, scheduleSeconds: Long): UUID {
    val workReq: WorkRequest = PeriodicWorkRequestBuilder<
            reminderWorker
            >(scheduleSeconds, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(ctx).enqueue(workReq)
    addRequestID(ctx, workReq.id)
    return workReq.id
}

fun reminderOneTimeSchedule(ctx: Context, delaySeconds: Long): UUID {
    val workReq: WorkRequest = OneTimeWorkRequestBuilder<
            reminderWorker
            >()
        .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(ctx).enqueue(workReq)
    addRequestID(ctx, workReq.id)
    return workReq.id
}

private fun getNextRequestCode(context: Context): Int {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
    val requestCode = sharedPreferences.getInt("requestCode", 0)
    sharedPreferences.edit().putInt("requestCode", requestCode + 1).apply()
    return requestCode
}

private fun getAllRequestCodes(context: Context): List<Int> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
    val requestCodes = sharedPreferences.getStringSet("requestCodes", emptySet())?.map { it.toInt() } ?: emptyList()
    return requestCodes
}

private fun addRequestCode(context: Context, requestCode: Int) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
    val requestCodes = sharedPreferences.getStringSet("requestCodes", emptySet())?.toMutableSet() ?: mutableSetOf()
    requestCodes.add(requestCode.toString())
    sharedPreferences.edit().putStringSet("requestCodes", requestCodes).apply()
}

private fun removeAllRequestCodes(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putStringSet("requestCodes", emptySet()).apply()
}

private fun removeRequestCode(context: Context, requestCode: Int) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
    val requestCodes = sharedPreferences.getStringSet("requestCodes", emptySet())?.toMutableSet() ?: mutableSetOf()
    requestCodes.remove(requestCode.toString())
    sharedPreferences.edit().putStringSet("requestCodes", requestCodes).apply()
}

fun reminderAlarmSchedule(ctx: Context, hour: Int, minute: Int): Int {
    try {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(ctx, reminderReceiver::class.java)

        val requestCode = getNextRequestCode(ctx)

        val pendingIntent = PendingIntent.getBroadcast(
            ctx,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        addRequestCode(ctx, requestCode)
        return requestCode
    } catch (e: SecurityException) {
        e.printStackTrace()
        Toast.makeText(ctx, "Permission required to set alarm", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        ctx.startActivity(intent)
    }
    return 0
}

fun reminderScheduleCancelAllPeriodic(ctx: Context) {
    WorkManager.getInstance(ctx).cancelAllWork()
    removeAllRequestIDs(ctx);
}

fun reminderScheduleCancelAllAlarm(ctx: Context) {
    val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val requestCodes = getAllRequestCodes(ctx)
    for (requestCode in requestCodes) {
        val intent = Intent(ctx, reminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(ctx, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }
    removeAllRequestCodes(ctx);
}

fun reminderScheduleCancelAll(ctx: Context) {
    reminderScheduleCancelAllPeriodic(ctx)
    reminderScheduleCancelAllAlarm(ctx)
}

fun reminderScheduleCancel(ctx: Context, id: UUID) {
    WorkManager.getInstance(ctx).cancelWorkById(id)
    removeRequestID(ctx, id)
}

fun reminderScheduleCancel(ctx: Context, requestCode: Int) {
    val intent = Intent(ctx, reminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(ctx, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    removeRequestCode(ctx, requestCode)
}