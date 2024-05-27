package com.wychlw.watertime.reminder

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.io.File
import java.util.UUID

class reminderRoute() {
    companion object {
        val REMINDER = "reminder"
        val ADD_ALARM = "add_alarm"
        val ADD_DELAY = "add_delay"
    }
}

class reminderMode() {
    companion object{
        val TIMING = 0
        val INTERVAL = 1
    }
}

data class periodicReminder(
    val period: Long,
    var id: UUID
)

data class timingReminder(
    val hour: Int,
    val minute: Int,
    var code: Int
)

data class reminderUiState(
    val mode: MutableState<Int>,
    val interval: MutableState<Int>,
    val timing: MutableState<List<Int>>,
    val intervalList: MutableState<List<periodicReminder>>,
    val timingList: MutableState<List<timingReminder>>,
    val intervalFile: MutableState<File>,
    val timingFile: MutableState<File>,
    val nav: NavController
)


fun getReminderMode(ctx: Context): Int {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("reminderMode", reminderMode.TIMING)
}


fun setReminderMode(ctx: Context, mode: Int) {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("reminderMode", mode).apply()
}


fun getReminderInterval(ctx: Context): Int {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("reminderInterval", 0)
}


fun setReminderInterval(ctx: Context, interval: Int) {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("reminderInterval", interval).apply()
}


fun getReminderTiming(ctx: Context): List<Int> {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("reminderTiming", "0,0")!!.split(",").map { it.toInt() }
}


fun setReminderTiming(ctx: Context, timing: List<Int>) {
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("reminderTiming", timing.joinToString(",")).apply()
}

fun saveState(ctx: Context, reminderState: MutableState<reminderUiState>) {
    setReminderMode(ctx, reminderState.value.mode.value)
    setReminderInterval(ctx, reminderState.value.interval.value)
    setReminderTiming(ctx, reminderState.value.timing.value)
}

@Composable
fun initReminderUiState(nav: NavController): MutableState<reminderUiState> {
    val ctx = LocalContext.current
    val mode = getReminderMode(ctx)
    val interval = getReminderInterval(ctx)
    val timing = getReminderTiming(ctx)

    val intervalFileName = "intervalState"
    val timingFileName = "timingState"
    val intervalFile = File(LocalContext.current.filesDir, intervalFileName)
    val timingFile = File(LocalContext.current.filesDir, timingFileName)

    val reminderState = remember {
        mutableStateOf(reminderUiState(
            mode = mutableStateOf(mode),
            interval = mutableStateOf(interval),
            timing = mutableStateOf(timing),
            intervalList = mutableStateOf(
                emptyList<periodicReminder>()
            ),
            timingList = mutableStateOf(
                emptyList<timingReminder>()
            ),
            intervalFile = mutableStateOf(intervalFile),
            timingFile = mutableStateOf(timingFile),
            nav = nav
        ))
    }

    reminderState.value.intervalList.value = getAllPeriodicReminders(state = reminderState)
    reminderState.value.timingList.value = getAllTimingReminders(state = reminderState)

    return reminderState
}