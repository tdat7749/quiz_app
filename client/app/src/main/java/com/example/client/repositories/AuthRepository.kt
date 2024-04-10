package com.example.client.repositories

import com.example.client.model.*
import com.example.client.network.auth.AuthService
import com.example.client.network.user.UserService
import com.example.client.utils.ResourceState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth
){
    suspend fun register(register: Register) = ApiHelper.safeCallApi {
        authService.register(register)
    }

    suspend fun login(login:Login) = ApiHelper.safeCallApi {
        authService.login(login)
    }

    suspend fun resendEmail(resendEmail: ResendEmail) = ApiHelper.safeCallApi {
        authService.resendEmail(resendEmail)
    }

    suspend fun verify(verify: Verify) = ApiHelper.safeCallApi {
        authService.verify(verify)
    }

    suspend fun loginWithGoogle(token:String) = ApiHelper.safeCallApi {
        authService.loginWithGoogle(token)
    }

    suspend fun googleSignIn(credential: AuthCredential) = ApiHelper.safeCallApi {
        firebaseAuth.signInWithCredential(credential).await()
    }

    suspend fun googleLogout() = ApiHelper.safeCallApi {
        firebaseAuth.signOut()
    }
}