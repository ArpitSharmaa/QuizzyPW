package com.example.quizzy.home

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizzy.R
import com.example.quizzy.login.LoginViewModel
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.model_class.*
import com.example.quizzy.navigation.NavigationRouts
import com.example.quizzy.page_state.HomeState
import com.example.quizzy.ui.theme.ColorRedAccuracy
import com.example.quizzy.ui.theme.DividerGradient
import com.example.quizzy.ui.theme.GrayPwBorder
import com.example.quizzy.ui.theme.GreenAssignment
import com.example.quizzy.ui.theme.OrangeQuiz
import com.example.quizzy.ui.theme.PurpleFoccused
import com.example.quizzy.ui.theme.TextColorPrimary
import com.example.quizzy.ui.theme.TextColorSecondary
import com.example.quizzy.util.SetStatusBarColor
import com.example.quizzy.util.interaction
import com.example.quizzy.util.interactionWithoutPadding
import com.example.quizzy.util.shimmerEffect
import kotlinx.coroutines.delay


@Composable
fun HomePage(
    navHostController: NavHostController,
    viewModel: HomeScreenViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    paddingValues: PaddingValues
) {
    val data by viewModel.dashboardResponse.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(data) {
        if (data is HomeState.Error) {
            Toast.makeText(context, (data as HomeState.Error).message, Toast.LENGTH_SHORT).show()
            delay(2000)
        }
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.setDashData(
            navHostController.previousBackStackEntry?.savedStateHandle?.get<DashboardResponse>(
                AuthState.SuccessOverDashBoard.DASHBOARD_ARGS
            )
        )
    }

    val state by loginViewModel.loginState.collectAsState()

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
    when (data) {
        is HomeState.Idle -> {
            HomeScreenShimmer()
        }

        is HomeState.Loading -> {
            HomeScreenShimmer()
        }

        is HomeState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hello Student!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextColorPrimary
                        )
                        Text(
                            text = "Click here to retry",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextColorSecondary,
                            modifier = Modifier.interactionWithoutPadding {
                                viewModel.setDashData(null)
                            }
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.logout_ic),
                        contentDescription = "logout",
                        modifier = Modifier
                            .interaction {
                                loginViewModel.logoutUser()
                            },
                        tint = ColorRedAccuracy,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Image(
                        painter = painterResource(R.drawable.search_avatar),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        "No data for the student found",
                        fontSize = 16.sp,
                        color = TextColorPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        is HomeState.OnSuccess -> {
            HomeScreen(
                navHostController,
                viewModel,
                (data as HomeState.OnSuccess).data,
                scrollState,
                paddingValues
            )
        }
    }

}

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    viewModel: HomeScreenViewModel = koinViewModel(),
    data: DashboardResponse,
    scrollState: ScrollState,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello ${data.student?.name ?: "Student"}!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextColorPrimary
                )
                Text(
                    text = data.student?.classOfStudent ?: "",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextColorSecondary
                )
            }
            Icon(
                painter = painterResource(R.drawable.notification_ic),
                contentDescription = "Notifications",
                modifier = Modifier
                    .interaction {
                        navHostController.navigate(NavigationRouts.NotificationScreen.routs)
                    },
                tint = TextColorPrimary,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Availability",
                value = data.student?.availability?.status ?: "N/A",
                color = GreenAssignment,
                bgColor = GreenAssignment.copy(0.2f),
                icon = R.drawable.check_ic,
                bottomTextColor = GreenAssignment
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Quiz",
                value = "${data.student?.quiz?.attempts ?: 0} Attempt",
                color = OrangeQuiz,
                bgColor = OrangeQuiz.copy(0.2f),
                icon = R.drawable.quick_chat,
                bottomTextColor = TextColorPrimary
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Accuracy",
                value = data.student?.accuracy?.current ?: "0%",
                color = ColorRedAccuracy,
                bgColor = ColorRedAccuracy.copy(0.2f),
                icon = R.drawable.accuracy_ic,
                bottomTextColor = TextColorPrimary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Today's Summary",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp),
            color = TextColorPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PurpleFoccused.copy(0.2f)),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                PurpleFoccused
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.search_avatar),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = data.todaySummary?.mood ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleFoccused
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"${data.todaySummary?.description ?: ""}\"",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = TextColorPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = TextColorPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        data.todaySummary?.recommendedVideo?.actionText ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Weekly Overview",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp),
            color = TextColorPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, GrayPwBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quiz Streak",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColorPrimary
                    )
                    Image(
                        painter = painterResource(R.drawable.question_card),
                        contentDescription = "question_cards"
                    )

                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.background(brush = DividerGradient)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    data.weeklyOverview?.quizStreak?.forEach { streak ->
                        StreakCircle(day = streak.day, status = streak.status)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Accuracy",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColorPrimary
                    )
                    Image(
                        painter = painterResource(R.drawable.accuracy_board),
                        contentDescription = null,
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.background(brush = DividerGradient)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = data.weeklyOverview?.overallAccuracy?.label ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColorPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { (data.weeklyOverview?.overallAccuracy?.percentage ?: 0) / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = ColorRedAccuracy,
                    trackColor = ColorRedAccuracy.copy(0.2f),
                    strokeCap = StrokeCap.Butt,
                    gapSize = 0.dp
                )
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Performance by Topic",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColorPrimary
                    )
                    Image(
                        painter = painterResource(R.drawable.performance_ic),
                        contentDescription = null
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.background(brush = DividerGradient)
                )
                Spacer(modifier = Modifier.height(16.dp))

                data.weeklyOverview?.performanceByTopic?.forEach {
                    PerformanceCard(
                        it?.topic,
                        it?.trend?.icon
                    )
                }
                Spacer(Modifier.height(6.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = TextColorPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("More Details")
                }
            }
        }
    }
}

@Composable
fun PerformanceCard(
    name: String?,
    @DrawableRes icon: Int?
) {
    icon?.let {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name ?: "",
                fontSize = 16.sp,
                color = TextColorPrimary
            )
            Image(
                painter = painterResource(icon),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color,
    bgColor: Color,
    @DrawableRes icon: Int,
    bottomTextColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextColorPrimary
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = bottomTextColor
            )
        }
    }
}

@Composable
fun StreakCircle(day: String?, status: StreakStatus?) {
    val color = when (status) {
        StreakStatus.COMPLETED -> GreenAssignment
        StreakStatus.PENDING -> TextColorSecondary
        null -> TextColorSecondary
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (status == StreakStatus.COMPLETED) color else Color.White)
                .drawBehind {
                    drawCircle(
                        color = color,
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 10f),
                                0f
                            )
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (status == StreakStatus.COMPLETED) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = day?.first()?.toString() ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColorSecondary
                )
            }
        }
    }
}

@Composable
fun HomeScreenShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) {
                StatCardshimmerEffect(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.LightGray.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.LightGray.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(7) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                repeat(3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun StatCardshimmerEffect(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(0.5f))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}