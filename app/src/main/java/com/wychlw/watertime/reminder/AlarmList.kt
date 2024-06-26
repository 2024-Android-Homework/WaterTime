package com.wychlw.watertime.reminder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AlarmList(modifier: Modifier = Modifier, state: MutableState<reminderUiState>) {
    val ctx = LocalContext.current
    OutlinedCard(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Text(
            modifier = modifier
                .padding(16.dp),
            text = "当前的闹钟",
            style = MaterialTheme.typography.titleMedium
        )

        if (state.value.timingList.value.isEmpty()) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier
                        .padding(16.dp),
                    text = "没有闹钟",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(state.value.timingList.value) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${
                                if (it.hour.toString().length < 2) "0" + it.hour.toString() else it.hour
                            }:${
                                if (it.minute.toString().length < 2) "0" + it.minute.toString() else it.minute
                            }",
                        )
                        IconButton(onClick = {
                            removeTimingReminder(ctx, state, it.code)
                            saveState(ctx, state)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "删除"
                            )
                        }
                    }

                }
            }
        }
    }
}