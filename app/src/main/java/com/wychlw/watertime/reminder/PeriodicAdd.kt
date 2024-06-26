package com.wychlw.watertime.reminder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PeriodicAdd(modifier: Modifier, state: MutableState<reminderUiState>) {
    FilledTonalIconButton(
        onClick = {
            state.value.nav.navigate(reminderRoute.ADD_DELAY)
        },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Periodic"
        )
    }
}