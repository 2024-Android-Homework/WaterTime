package com.wychlw.watertime.reminder

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ReminderModeSelector(modifier: Modifier = Modifier, state: MutableState<reminderUiState>) {
    val ctx = LocalContext.current
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = modifier
                .padding(start = 16.dp, end = 8.dp),
            text = "提醒方式"
        )
        Button(
            onClick = {
                if (state.value.mode.value != reminderMode.TIMING) {
                    state.value.mode.value = reminderMode.TIMING
                    reminderScheduleCancelAllPeriodic(ctx)
                    for (alarm in state.value.timingList.value) {
                        alarm.code = reminderAlarmSchedule(ctx, alarm.hour, alarm.minute)
                    }
                }
                saveState(ctx, reminderState = state)
            },
            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (state.value.mode.value == reminderMode.TIMING)  Color(0xFF6750A4) else Color(0xFFEADDFF),
                contentColor =
                if (state.value.mode.value == reminderMode.TIMING)  Color(0xFFFFFFFF) else Color(0xFF000000),
            ),
            elevation = ButtonDefaults.filledTonalButtonElevation(),
            border = ButtonDefaults.outlinedButtonBorder,
            contentPadding = ButtonDefaults.ContentPadding,
        ) {
            Text(
                text = "定时"
            )
        }

        Button(
            onClick = {
                if (state.value.mode.value != reminderMode.INTERVAL) {
                    state.value.mode.value = reminderMode.INTERVAL
                    reminderScheduleCancelAllPeriodic(ctx)
                    for (periodic in state.value.intervalList.value) {
                        periodic.id = reminderPeriodicSchedule(ctx, periodic.period)
                    }
                }
                saveState(ctx, reminderState = state)
            },
            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (state.value.mode.value == reminderMode.INTERVAL)  Color(0xFF6750A4) else Color(0xFFEADDFF),
                contentColor =
                if (state.value.mode.value == reminderMode.INTERVAL)  Color(0xFFFFFFFF) else Color(0xFF000000),
            ),
            elevation = ButtonDefaults.filledTonalButtonElevation(),
            border = ButtonDefaults.outlinedButtonBorder,
            contentPadding = ButtonDefaults.ContentPadding,
        ) {
            Text(
                text = "间隔"
            )
        }
    }
}