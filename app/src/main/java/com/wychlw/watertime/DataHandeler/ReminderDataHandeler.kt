package com.wychlw.watertime.DataHandeler

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wychlw.watertime.reminder.periodicReminder
import com.wychlw.watertime.reminder.timingReminder
import java.io.File
import kotlin.time.Duration

class ReminderDataHandeler {

    private val AlarmFile: File
    private val DurationFile: File

    constructor(ctx: Context) {
        AlarmFile = File(ctx.filesDir, "intervalState")
        DurationFile = File(ctx.filesDir, "timingState")

    }

    fun loadSetting(): Pair<List<timingReminder>, List<periodicReminder>> {
        val gson = Gson()
        val timingList: List<timingReminder> = if (DurationFile.exists()) {
            val timingJson = DurationFile.readText()
            gson.fromJson(timingJson, object : TypeToken<List<timingReminder>>() {}.type)
        } else {
            mutableListOf()
        }
        val intervalList: List<periodicReminder> = if (AlarmFile.exists()) {
            val intervalJson = AlarmFile.readText()
            gson.fromJson(intervalJson, object : TypeToken<List<periodicReminder>>() {}.type)
        } else {
            mutableListOf()
        }
        return Pair(timingList, intervalList)
    }

    fun saveTimingSetting(timingList: List<timingReminder>) {
        val gson = Gson()
        val timingJson = gson.toJson(timingList)
        DurationFile.writeText(timingJson)
    }

    fun saveIntervalSetting(intervalList: List<periodicReminder>) {
        val gson = Gson()
        val intervalJson = gson.toJson(intervalList)
        AlarmFile.writeText(intervalJson)
    }

    fun saveSetting(timingList: List<timingReminder>, intervalList: List<periodicReminder>) {
        saveTimingSetting(timingList)
        saveIntervalSetting(intervalList)
    }
}