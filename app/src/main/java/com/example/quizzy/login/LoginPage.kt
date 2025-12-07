package com.example.quizzy.login

import android.graphics.Bitmap
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.quizzy.R
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.navigation.NavigationRouts
import com.example.quizzy.ui.theme.BlueBubble
import com.example.quizzy.ui.theme.ContainerColorEditor
import com.example.quizzy.ui.theme.GreenBubble
import com.example.quizzy.ui.theme.PinkBubble
import com.example.quizzy.ui.theme.StrokeOutLinedEditor
import com.example.quizzy.ui.theme.TextColorPrimary
import com.example.quizzy.ui.theme.TextColorSecondary
import com.example.quizzy.ui.theme.YellowBubble
import com.example.quizzy.util.CharLimits
import com.example.quizzy.util.SetStatusBarColor
import com.example.quizzy.util.interaction
import com.example.quizzy.util.interactionWithoutPadding
import com.example.quizzy.util.shimmerEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginPage(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
    paddingValues: PaddingValues
) {
    val state by viewModel.loginState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state is AuthState.ErrorOverAuth) {
            Toast.makeText(
                context,
                (state as AuthState.ErrorOverAuth).error?.message ?: "Unknown Error",
                Toast.LENGTH_SHORT
            ).show()
        } else if (state is AuthState.SuccessOverAuth) {
            Toast.makeText(
                context,
                (state as AuthState.SuccessOverAuth).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    SetStatusBarColor(useDarkIcons = false)

    when (state) {
        is AuthState.Idle -> QuizzyLoginScreen(
            viewModel,
            navHostController,
            paddingValues = paddingValues
        )

        is AuthState.SuccessOverDashBoard -> {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                AuthState.SuccessOverDashBoard.DASHBOARD_ARGS,
                (state as AuthState.SuccessOverDashBoard).dashboardResponse
            )
            navHostController.navigate(NavigationRouts.HomeGraph.routs) {
                popUpTo(NavigationRouts.AuthGraph.routs) {
                    inclusive = true
                }
            }
        }

        is AuthState.Loading -> {
            QuizzyLoginScreen(viewModel, navHostController, true, paddingValues = paddingValues)
        }

        is AuthState.SuccessOverAuth -> {
            navHostController.navigate(NavigationRouts.HomeGraph.routs)
        }

        is AuthState.ErrorOverAuth -> {
            QuizzyLoginScreen(viewModel, navHostController, paddingValues = paddingValues)
        }
    }

}

@Composable
fun QuizzyLoginScreen(
    viewModel: LoginViewModel,
    navHostController: NavHostController,
    isShimmer: Boolean = false,
    paddingValues: PaddingValues
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        DecorativeBackground()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.53f)
                    .fillMaxWidth()
            ) {
                HeroSection("Welcome to\nQuizzy!")
            }

            Box(
                modifier = Modifier
                    .weight(0.47f)
                    .fillMaxWidth()
            ) {
                if (isShimmer) {
                    ShimmerLogin(viewModel)
                } else {
                    LoginFormSection(
                        viewModel,
                        navHostController
                    )
                }
            }
        }
    }
}


@Composable
fun HeroSection(
    heading: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            AvatarCircle(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-60).dp, y = (12).dp),
                color = PinkBubble,
                size = 140.dp,
                R.drawable.student_man
            )
            AvatarCircle(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (70).dp, y = (6).dp),
                color = BlueBubble,
                size = 115.dp,
                R.drawable.student_avatar
            )
            AvatarCircle(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (40).dp),
                color = GreenBubble,
                size = 110.dp,
                R.drawable.girl_student
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = heading,
            fontSize = 28.sp,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun AvatarCircle(
    modifier: Modifier = Modifier,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    @DrawableRes res: Int
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = res),
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ShimmerLogin(
    viewModel: LoginViewModel
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
            .fillMaxSize(),
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = 40.dp,
            topEnd = 40.dp,
            bottomEnd = 40.dp,
            bottomStart = 40.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Let's Get you Signed in",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextColorPrimary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                QuizzyTextField(
                    value = viewModel.email,
                    onValueChange = { },
                    placeholder = "Email",
                    isError = false,
                    enabled = false,
                    supportingText = {
                        Text("Invalid email address characters limit from ${CharLimits.EMAIL_LIMIT_MIN} - ${CharLimits.EMAIL_LIMIT_MAX} characters")
                    }

                )

                Spacer(modifier = Modifier.height(16.dp))

                QuizzyTextField(
                    value = viewModel.password,
                    onValueChange = { },
                    placeholder = "Password",
                    isLast = true,
                    isError = false,
                    enabled = false,
                    supportingText = {
                        Text("Invalid password  characters limit from ${CharLimits.PASSWORD_LIMIT_MIN} - ${CharLimits.PASSWORD_LIMIT_MAX} characters")
                    }
                )
                Text(
                    "Don't have an account? Register",
                    color = TextColorSecondary,
                    fontSize = 13.sp
                )
                Text("Forgot Password?", color = TextColorSecondary, fontSize = 13.sp)

                Spacer(Modifier.height(16.dp))
            }
            QuizzyButton(
                "Sign In",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}


@Composable
fun LoginFormSection(
    viewModel: LoginViewModel,
    navHostController: NavHostController
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
            .fillMaxSize(),
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = 40.dp,
            topEnd = 40.dp,
            bottomEnd = 40.dp,
            bottomStart = 40.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Let's Get you Signed in",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextColorPrimary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                QuizzyTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.setEmailText(it) },
                    placeholder = "Email",
                    isError = viewModel.emailError,
                    supportingText = {
                        Text("Invalid email address characters limit from ${CharLimits.EMAIL_LIMIT_MIN} - ${CharLimits.EMAIL_LIMIT_MAX} characters")
                    }

                )

                Spacer(modifier = Modifier.height(16.dp))

                QuizzyTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.setPasswordText(it) },
                    placeholder = "Password",
                    isLast = true,
                    isError = viewModel.passwordError,
                    supportingText = {
                        Text("Invalid password  characters limit from ${CharLimits.PASSWORD_LIMIT_MIN} - ${CharLimits.PASSWORD_LIMIT_MAX} characters")
                    }
                )

                Text(
                    "Don't have an account? Register",
                    color = TextColorSecondary,
                    modifier = Modifier.interaction {
                        navHostController.navigate(NavigationRouts.SignUp.routs)
                    },
                    fontSize = 13.sp
                )
                Text(
                    "Forgot Password?",
                    color = TextColorSecondary,
                    modifier = Modifier.interaction {
                        navHostController.navigate(NavigationRouts.ForgotPassword.routs)
                    },
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(16.dp))
            }
            QuizzyButton(
                "Sign In",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomCenter)
                    .interactionWithoutPadding {
                        if (!viewModel.emailError && !viewModel.passwordError) viewModel.loginUser() else {
                            viewModel.setEmailText(viewModel.email)
                            viewModel.setPasswordText(viewModel.password)
                        }
                    }
            )
        }
    }
}

@Composable
fun QuizzyButton(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .height(70.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(size.width, size.height)
                lineTo(size.width, size.height * 1f)

                cubicTo(
                    size.width * 0.65f, size.height * 1f,
                    size.width * 0.7f, 0f,
                    size.width * 0.5f, 0f
                )
                cubicTo(
                    size.width * 0.3f, 0f,
                    size.width * 0.35f, size.height * 1f,
                    0f, size.height * 1f
                )
                close()
            }
            drawPath(path = path, color = Color.Black)
        }

        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        )
    }
}

@Composable
fun QuizzyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isLast: Boolean = false,
    isError: Boolean,
    enabled: Boolean = true,
    supportingText: @Composable () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ContainerColorEditor,
            unfocusedContainerColor = ContainerColorEditor,
            disabledContainerColor = ContainerColorEditor,
            focusedBorderColor = StrokeOutLinedEditor,
            unfocusedBorderColor = StrokeOutLinedEditor,
            focusedTextColor = TextColorPrimary,
            unfocusedTextColor = TextColorPrimary
        ),
        placeholder = {
            Text(text = placeholder, color = TextColorSecondary)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        isError = isError,
        supportingText = if (isError) supportingText else null,
        enabled = enabled
    )
}

@Preview
@Composable
fun DecorativeBackground() {
    val context = LocalContext.current

    val piIconBitmap: ImageBitmap? = remember(context) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.pi)
        drawable?.let {
            val width = 60
            val height = 60
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            bitmap.asImageBitmap()
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        drawCircle(
            color = PinkBubble,
            radius = 60f,
            center = Offset(0f, height * 0.1f)
        )
        drawCircle(
            color = GreenBubble,
            radius = 15f,
            center = Offset(width * 0.65f, height * 0.05f)
        )

        drawCircle(
            color = GreenBubble,
            radius = 80f,
            center = Offset(width, height * 0.05f)
        )

        drawCircle(
            color = BlueBubble,
            radius = 15f,
            center = Offset(60f, height * 0.35f)
        )

        piIconBitmap?.let {
            drawImage(
                image = piIconBitmap,
                topLeft = Offset(2f, height * 0.43f)
            )
        }
        drawCircle(
            color = YellowBubble,
            radius = 15f,
            center = Offset(width * 0.92f, height * 0.4f)
        )
    }
}
