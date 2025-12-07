package com.example.quizzy.signup

import com.example.quizzy.login.LoginViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizzy.login.DecorativeBackground
import com.example.quizzy.login.HeroSection
import com.example.quizzy.login.QuizzyButton
import com.example.quizzy.login.QuizzyTextField
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.navigation.NavigationRouts
import com.example.quizzy.ui.theme.TextColorPrimary
import com.example.quizzy.ui.theme.TextColorSecondary
import com.example.quizzy.util.CharLimits
import com.example.quizzy.util.SetStatusBarColor
import com.example.quizzy.util.interaction
import com.example.quizzy.util.interactionWithoutPadding
import com.example.quizzy.util.shimmerEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignupPage(
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
        is AuthState.Idle -> QuizzySignUpScreen(
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
            QuizzySignUpScreen(viewModel, navHostController, true, paddingValues = paddingValues)
        }

        is AuthState.SuccessOverAuth -> {
            navHostController.navigate(NavigationRouts.HomeGraph.routs)
        }

        is AuthState.ErrorOverAuth -> {
            QuizzySignUpScreen(viewModel, navHostController, paddingValues = paddingValues)
        }
    }

}

@Composable
fun QuizzySignUpScreen(
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
                HeroSection("Register to\nQuizzy!")
            }

            Box(
                modifier = Modifier
                    .weight(0.47f)
                    .fillMaxWidth()
            ) {
                if (isShimmer) {
                    ShimmerLogin(viewModel)
                } else {
                    SignUpFormSection(
                        viewModel,
                        navHostController
                    )
                }
            }
        }
    }
}


@Composable
fun ShimmerLogin(viewModel: LoginViewModel) {
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
                    "Already have an account? Sign In",
                    color = TextColorSecondary,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(16.dp))
            }
            QuizzyButton(
                "Sign Up",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}


@Composable
fun SignUpFormSection(
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
                    "Already have an account? Sign In",
                    fontSize = 13.sp,
                    color = TextColorSecondary,
                    modifier = Modifier.interaction {
                        navHostController.navigateUp()
                    })
                Spacer(Modifier.height(16.dp))
            }
            QuizzyButton(
                "Sign Up",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomCenter)
                    .interactionWithoutPadding {
                        if (!viewModel.emailError && !viewModel.passwordError) viewModel.signUpUser() else {
                            viewModel.setEmailText(viewModel.email)
                            viewModel.setPasswordText(viewModel.password)
                        }
                    }
            )
        }
    }
}



