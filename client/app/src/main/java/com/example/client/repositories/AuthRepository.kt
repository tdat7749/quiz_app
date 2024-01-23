package com.example.client.repositories

import com.example.client.model.AuthToken
import com.example.client.network.auth.AuthService
import com.example.client.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
){
    suspend fun login(userName:String, password:String): Flow<ResourceState<AuthToken>> {
        return flow {
            emit(ResourceState.Loading())

            val response = authService.login(userName,password)
            if(response.isSuccessful){
                emit(ResourceState.Success(response.body()!!))
            }else{
                emit(ResourceState.Error("1"))
            }

            emit(ResourceState.Success(response.body()!!))
        }.catch {
            emit(ResourceState.Error("Lỗi con mẹ mày rồi ?"))
        }
    }
}