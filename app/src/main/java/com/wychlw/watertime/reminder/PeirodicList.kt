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
import androidx.work.PeriodicWorkRequest

@Composable
fun PeriodicList(modifier: Modifier = Modifier, state: MutableState<reminderUiState>) {
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
            text = "当前的间隔",
            style = MaterialTheme.typography.titleMedium
        )

        if (state.value.intervalList.value.isEmpty()) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier
                        .padding(16.dp),
                    text = "没有间隔",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val list = state.value.intervalList.value
                val sorted = list.sortedBy {
                    it.period
                }
                items(sorted) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${it.period/60}分钟",
                        )
                        IconButton(onClick = {
                            removePeriodicReminder(ctx, state, it.id)
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