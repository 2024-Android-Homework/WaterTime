package com.wychlw.watertime.reminder

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.util.UUID

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
    val timingFile: MutableState<File>
)

@Composable
private fun getReminderMode(): Int {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("reminderMode", reminderMode.TIMING)
}

@Composable
private fun setReminderMode(mode: Int) {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("reminderMode", mode).apply()
}

@Composable
private fun getReminderInterval(): Int {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("reminderInterval", 0)
}

@Composable
private fun setReminderInterval(interval: Int) {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("reminderInterval", interval).apply()
}

@Composable
private fun getReminderTiming(): List<Int> {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("reminderTiming", "0,0")!!.split(",").map { it.toInt() }
}

@Composable
private fun setReminderTiming(timing: List<Int>) {
    val ctx = LocalContext.current
    val sharedPreferences = ctx.getSharedPreferences("reminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("reminderTiming", timing.joinToString(",")).apply()
}

@Composable
fun saveState(reminderState: MutableState<reminderUiState>) {
    setReminderMode(reminderState.value.mode.value)
    setReminderInterval(reminderState.value.interval.value)
    setReminderTiming(reminderState.value.timing.value)
}

@Composable
fun initReminderUiState(): MutableState<reminderUiState> {
    val mode = getReminderMode()
    val interval = getReminderInterval()
    val timing = getReminderTiming()

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
            timingFile = mutableStateOf(timingFile)
        ))
    }

    reminderState.value.intervalList.value = getAllPeriodicReminders(state = reminderState)
//    reminderState.value.timingList.value =

    return reminderState
}