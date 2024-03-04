package com.example.client.di

import android.util.Log
import com.example.client.utils.AppConstants
import com.example.client.network.ApiService
import com.example.client.network.auth.AuthService
import com.example.client.network.quiz.QuizService
import com.example.client.network.topic.TopicService
import com.example.client.network.user.UserService
import com.example.client.repositories.AuthRepository
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.TopicRepository
import com.example.client.repositories.UserRepository
import com.example.client.utils.SharedPreferencesManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesRetrofit() : Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }


        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor{ chain ->
                val originalRequest = chain.request()
                val accessToken =  SharedPreferencesManager.getAccessToken()
                Log.d("URL",originalRequest.url.encodedPath)
                Log.d("ACCESS_TOKEN",accessToken.toString())
                if(AppConstants.PUBLIC_API.contains(originalRequest.url.encodedPath)){
                    chain.proceed(originalRequest)
                }else{
                    val requestWithJwt = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                    chain.proceed(requestWithJwt)
                }
            }
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(AppConstants.APP_BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesApiClient(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit) : AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authService: AuthService) : AuthRepository {
        return AuthRepository(authService)
    }

    @Provides
    @Singleton
    fun providesQuizService(retrofit: Retrofit) : QuizService {
        return retrofit.create(QuizService::class.java)
    }

    @Provides
    @Singleton
    fun providesQuizRepository(quizService: QuizService) : QuizRepository {
        return QuizRepository(quizService)
    }

    @Provides
    @Singleton
    fun providesTopicService(retrofit: Retrofit) : TopicService {
        return retrofit.create(TopicService::class.java)
    }

    @Provides
    @Singleton
    fun providesTopicRepository(topicService: TopicService) : TopicRepository {
        return TopicRepository(topicService)
    }

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit) : UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserRepository(userService: UserService) : UserRepository {
        return UserRepository(userService)
    }
}