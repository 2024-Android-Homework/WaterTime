package com.wychlw.watertime.reminder

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wychlw.watertime.reminder.ReminderAdd.AddAlarmView
import com.wychlw.watertime.reminder.ReminderAdd.AddDelayView
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
    val nav = rememberNavController()
    val state = initReminderUiState(nav)

    AppTheme {
        NavHost(
            navController = nav,
            startDestination = reminderRoute.REMINDER,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                ExitTransition.None
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            popEnterTransition = {
                EnterTransition.None
            }
        ) {
            composable(route = reminderRoute.REMINDER) {
                ReminderView(state = state)
            }
            composable(route = reminderRoute.ADD_ALARM) {
                AddAlarmView(state = state)
            }
            composable(route = reminderRoute.ADD_DELAY) {
                AddDelayView(state = state)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReminderActivityCreate()
}