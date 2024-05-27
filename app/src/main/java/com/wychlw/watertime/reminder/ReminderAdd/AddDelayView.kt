package com.wychlw.watertime.reminder.ReminderAdd

import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wychlw.watertime.reminder.addPeriodicReminder
import com.wychlw.watertime.reminder.addTimingReminder
import com.wychlw.watertime.reminder.reminderUiState
import com.wychlw.watertime.reminder.saveState
import com.wychlw.watertime.reminder.util.AddThingTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDelayView(modifier: Modifier = Modifier, state: MutableState<reminderUiState>) {
    val timeState = rememberTimePickerState(0, 0, true)
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            AddThingTopBar(
                state = state,
                title = "添加间隔提醒"
            )
        }
    ) {innerPadding ->
        OutlinedCard(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = modifier
                    .padding(8.dp)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "选择间隔",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Divider(
                        modifier = modifier
                            .padding(vertical = 8.dp)
                    )
                    TimePicker(
                        state = timeState
                    )
                    Divider(
                        modifier = modifier
                            .padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = {
                                addPeriodicReminder(
                                    ctx,
                                    state,
                                    (((timeState.hour * 60) + timeState.minute) * 60).toLong()
                                )
                                state.value.nav.navigateUp()
                                saveState(ctx, state)
                            },
                            enabled = ((((timeState.hour * 60) + timeState.minute) * 60) >= 15)
                        ) {
                            if ((((timeState.hour * 60) + timeState.minute) * 60).toLong() < 15) {
                                Text(
                                    text = "间隔时间太短, 请至少设置15分钟以上的间隔"
                                )
                            } else {
                                Text(
                                    text = "添加间隔 ${
                                        if (timeState.hour.toString().length < 2) "0" + timeState.hour.toString() else timeState.hour
                                    }:${
                                        if (timeState.minute.toString().length < 2) "0" + timeState.minute.toString() else timeState.minute
                                    } 后的提醒"
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}