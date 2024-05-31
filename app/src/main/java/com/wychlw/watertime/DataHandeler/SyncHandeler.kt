package com.wychlw.watertime.DataHandeler

import android.content.Context
import com.google.gson.Gson
import com.wychlw.watertime.Record
import com.wychlw.watertime.reminder.periodicReminder
import com.wychlw.watertime.reminder.timingReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.UUID
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.GlobalScope

data class user_data (
    val records: List<Record>,
    val periodicReminders: List<periodicReminder>,
    val timingReminders: List<timingReminder>,
    val volume: Int
)

class SyncHandeler {

    private val uuid: UUID

    private val serverURL = "https://backend.lingwang.workers.dev/"

    constructor(ctx: Context) {
        val sharedPreferences = ctx.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val uuidString = sharedPreferences.getString("uuid", null)
        uuid = if (uuidString == null) {
            val newUUID = UUID.randomUUID()
            sharedPreferences.edit().putString("uuid", newUUID.toString()).apply()
            newUUID
        } else {
            UUID.fromString(uuidString)
        }
    }

    fun getUUID(): UUID {
        return uuid
    }

    fun putUUID(ctx: Context, newUUID: UUID) {
        val sharedPreferences = ctx.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("uuid", newUUID.toString()).apply()
    }

    fun syncToRemote(ctx: Context) =
        runBlocking(Dispatchers.IO) {
            val recordHandeler = RecordHandeler(ctx)
            val reminderDataHandeler = ReminderDataHandeler(ctx)
            val settingHandeler = SettingHandeler(ctx)

            val data = user_data(
                recordHandeler.loadRecord(),
                reminderDataHandeler.loadSetting().second,
                reminderDataHandeler.loadSetting().first,
                settingHandeler.loadSetting()
            )

            val gson = Gson()
            val json = gson.toJson(data)

            val client = OkHttpClient()
            val headers = okhttp3.Headers.Builder()
                .add("uuid", uuid.toString())
                .build()
            val body = json.toRequestBody("application/json".toMediaTypeOrNull())
            val request = okhttp3.Request.Builder()
                .url(serverURL)
                .headers(headers)
                .post(body)
                .build()
            client.newCall(request).execute()
        }


    fun syncFromRemote(ctx: Context): user_data? =

        runBlocking(Dispatchers.IO) {
            val recordHandeler = RecordHandeler(ctx)
            val reminderDataHandeler = ReminderDataHandeler(ctx)
            val settingHandeler = SettingHandeler(ctx)

            val client = OkHttpClient()
            val headers = okhttp3.Headers.Builder()
                .add("uuid", uuid.toString())
                .build()
            val request = okhttp3.Request.Builder()
                .url(serverURL)
                .headers(headers)
                .get()
                .build()
            val response = client.newCall(request).execute()
            val json = response.body?.string()
            val gson = Gson()
            val data = gson.fromJson(json, user_data::class.java)

            recordHandeler.saveRecord(data.records)
            reminderDataHandeler.saveSetting(data.timingReminders, data.periodicReminders)
            settingHandeler.saveSetting(data.volume)

            data
        }

}