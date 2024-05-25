package com.wychlw.watertime.reminder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderView(
    modifier: Modifier = Modifier,
    state: MutableState<reminderUiState>
) {
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            ReminderTopBar(modifier = modifier)
        }
    ) {innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                ReminderModeSelector(modifier = modifier, state = state)
                Divider(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                )

                if (state.value.mode.value == reminderMode.TIMING) {
                    AlarmList(modifier = modifier, state = state)
                } else {
                    PeriodicList(modifier = modifier, state = state)
                }

                Divider(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                )

                if (state.value.mode.value == reminderMode.TIMING) {
                    AlarmAdd(modifier = modifier, state = state)
                } else {
                    PeriodicAdd(modifier = modifier, state = state)
                }


            }
        }
    }
}