package com.wychlw.watertime.reminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wychlw.work1.AppTheme

class ReminderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReminderActivityCreate()
        }
    }
}

@Composable
fun ReminderActivityCreate() {
    val state = initReminderUiState()

    AppTheme {
        ReminderView(state = state)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReminderActivityCreate()
}