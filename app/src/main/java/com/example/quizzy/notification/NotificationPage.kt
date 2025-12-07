package com.example.quizzy.notification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizzy.R
import com.example.quizzy.login.LoginViewModel
import com.example.quizzy.navigation.NavigationRouts
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.ui.theme.ColorRedAccuracy
import com.example.quizzy.ui.theme.GreenAssignment
import com.example.quizzy.ui.theme.OrangeQuiz
import com.example.quizzy.ui.theme.PurpleFoccused
import com.example.quizzy.ui.theme.TextColorPrimary
import com.example.quizzy.ui.theme.TextColorSecondary
import com.example.quizzy.util.SetStatusBarColor
import com.example.quizzy.util.interaction
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsSettingsScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
    paddingValues: PaddingValues
) {

    val state by viewModel.loginState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is AuthState.SuccessOverAuth) {
            Toast.makeText(
                context,
                (state as AuthState.SuccessOverAuth).message,
                Toast.LENGTH_SHORT
            ).show()
            navHostController.navigate(NavigationRouts.AuthGraph.routs) {
                popUpTo(NavigationRouts.HomeGraph.routs) { inclusive = true }
            }
        } else if (state is AuthState.ErrorOverAuth) {
            val toastEvent = (state as AuthState.ErrorOverAuth).error?.message ?: "Unknown Error"
            Toast.makeText(context, toastEvent, Toast.LENGTH_SHORT).show()
        }
    }
    SetStatusBarColor(useDarkIcons = true)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.CenterStart).interaction {
                    navHostController.navigateUp()
                }
            )
            Text(
                text = "Notifications & Settings",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center),
                color = TextColorPrimary
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "Notifications",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColorPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                NotificationItem(
                    title = "Missed quiz in physics in yesterday",
                    time = "2 hours ago",
                    barColor = OrangeQuiz,
                    bgColor = OrangeQuiz.copy(0.2f)
                )
            }
            item {
                NotificationItem(
                    title = "Badge earned",
                    time = "8 hours ago",
                    barColor = PurpleFoccused,
                    bgColor = PurpleFoccused.copy(0.2f)
                )
            }
            item {
                NotificationItem(
                    title = "Teacher Note",
                    time = "1 day ago",
                    barColor = GreenAssignment,
                    bgColor = GreenAssignment.copy(0.2f)
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = TextColorPrimary
                )
            }

            item {
                SettingItem(
                    icon = R.drawable.people_ic,
                    title = "Switch Child",
                    subtitle = "Change active child profile",
                    isRed = false
                ) {

                }
            }
            item {
                SettingItem(
                    icon = R.drawable.language_ic,
                    title = "Language",
                    subtitle = "English",
                    isRed = false
                ) {

                }
            }
            item {
                SettingItem(
                    icon = R.drawable.logout_ic,
                    title = "Logout",
                    subtitle = "Sign out of your account",
                    isRed = true
                ) {
                    viewModel.logoutUser()
                }
            }
        }
    }
}


@Composable
fun NotificationItem(title: String, time: String, barColor: Color, bgColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(bgColor, RoundedCornerShape(4.dp))
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(barColor)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColorPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                fontSize = 12.sp,
                color = TextColorSecondary
            )
        }
    }
}

@Composable
fun SettingItem(icon: Int, title: String, subtitle: String, isRed: Boolean, onCLick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .interaction {
                onCLick.invoke()
            }
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (isRed) ColorRedAccuracy else TextColorPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isRed) ColorRedAccuracy else TextColorPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextColorSecondary
            )
        }
    }
}