package com.wychlw.watertime.reminder

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.time.Period
import java.util.UUID

fun savePeriodicReminder(state: MutableState<reminderUiState>) {
    val file = state.value.intervalFile.value
    val list = state.value.intervalList.value

    val gson = Gson()
    val json = gson.toJson(list)

    file.writeText(json)
}

fun getAllPeriodicReminders(state: MutableState<reminderUiState>): List<periodicReminder> {
    val file = state.value.intervalFile.value
    return if (file.exists()) {
        val gson = Gson()
        val json = file.readText()
        val typeToken = object : TypeToken<List<periodicReminder>>() {}
        gson.fromJson(json, typeToken.type)
    } else {
        mutableListOf()
    }
}

fun addPeriodicReminder(ctx: Context, state: MutableState<reminderUiState>, period: Long) {
    for (reminder in state.value.intervalList.value) {
        if (reminder.period == period) {
            return
        }
    }
    val id = reminderPeriodicSchedule(ctx, period)
    val newReminder = periodicReminder(period, id)
    state.value.intervalList.value += newReminder
    savePeriodicReminder(state)
}

fun removeAllPeriodicReminders(ctx: Context, state: MutableState<reminderUiState>) {
    reminderScheduleCancelAllPeriodic(ctx)
    state.value.intervalList.value = emptyList()
    savePeriodicReminder(state)
}

fun removePeriodicReminder(ctx: Context, state: MutableState<reminderUiState>, id: UUID) {
    for (reminder in state.value.intervalList.value) {
        if (reminder.id == id) {
            state.value.intervalList.value -= reminder
            reminderScheduleCancel(ctx, id)
            savePeriodicReminder(state)
            return
        }
    }
}

fun saveTimingReminder(state: MutableState<reminderUiState>) {
    val file = state.value.timingFile.value
    val list = state.value.timingList.value

    val gson = Gson()
    val json = gson.toJson(list)

    file.writeText(json)
}

fun getAllTimingReminders(state: MutableState<reminderUiState>): List<timingReminder> {
    val file = state.value.timingFile.value
    return if (file.exists()) {
        val gson = Gson()
        val json = file.readText()
        val typeToken = object : TypeToken<List<timingReminder>>() {}
        gson.fromJson(json, typeToken.type)
    } else {
        mutableListOf()
    }
}

fun addTimingReminder(ctx: Context, state: MutableState<reminderUiState>, hour: Int, minute: Int) {
    for (reminder in state.value.timingList.value) {
        if (reminder.hour == hour && reminder.minute == minute) {
            return
        }
    }
    val requestCode = reminderAlarmSchedule(ctx, hour, minute)
    val newReminder = timingReminder(hour, minute, requestCode)
    state.value.timingList.value += newReminder
    saveTimingReminder(state)
}

fun removeAllTimingReminders(ctx: Context, state: MutableState<reminderUiState>) {
    reminderScheduleCancelAllAlarm(ctx)
    state.value.timingList.value = emptyList()
    saveTimingReminder(state)
}

fun removeTimingReminder(ctx: Context, state: MutableState<reminderUiState>, requestCode: Int) {
    for (reminder in state.value.timingList.value) {
        if (reminder.code == requestCode) {
            state.value.timingList.value -= reminder
            reminderScheduleCancel(ctx, requestCode)
            saveTimingReminder(state)
            return
        }
    }
}

