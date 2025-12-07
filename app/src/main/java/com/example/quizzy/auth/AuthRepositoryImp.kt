package com.example.quizzy.auth

import com.example.quizzy.page_state.AuthState
import com.example.quizzy.retrofit.ApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun logIn(email: String, password: String): AuthState {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (ex: Exception) {
            return AuthState.ErrorOverAuth(ex)
        }
        return try {
            val result = apiService.getDashBoardData()
            if (result.isSuccessful && result.body() != null) {
                AuthState.SuccessOverDashBoard(
                    result.body()!!
                )
            } else {
                AuthState.SuccessOverAuth(
                    "Logged In Successfully"
                )
            }
        } catch (ex: Exception) {
            AuthState.SuccessOverAuth("Logged In Successfully")
        }


    }

    override suspend fun signUp(email: String, password: String): AuthState {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            return AuthState.ErrorOverAuth(e)
        }
        return try {
            val result = apiService.getDashBoardData()
            if (result.isSuccessful && result.body() != null) {
                AuthState.SuccessOverDashBoard(result.body()!!)
            } else {
                AuthState.SuccessOverAuth("SignUp Successful")
            }
        } catch (e: Exception) {
            AuthState.SuccessOverAuth("SignUp Successful")
        }
    }

    override suspend fun forgotPassword(email: String): AuthState {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            return AuthState.ErrorOverAuth(e)
        }
        return AuthState.SuccessOverAuth("Password Reset Link Sent")
    }

    override suspend fun logOut(): AuthState {
        try {
            firebaseAuth.signOut()
            return AuthState.SuccessOverAuth(
                "Successfully Logged Out"
            )
        } catch (ex: Exception) {
            return AuthState.ErrorOverAuth(
                ex
            )
        }
    }

    override suspend fun isLoggedIn(): AuthState {
        return if (firebaseAuth.currentUser != null) {
            AuthState.SuccessOverAuth(
                "User Logged In"
            )
        } else {
            AuthState.ErrorOverAuth(
                Exception("Logged in session has expired please login again")
            )
        }
    }
}