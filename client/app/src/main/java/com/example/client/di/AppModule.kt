package com.example.client.di

import android.util.Log
import com.example.client.adapter.DateJsonAdapter
import com.example.client.model.QuestionType
import com.example.client.utils.AppConstants
import com.example.client.network.ApiService
import com.example.client.network.auth.AuthService
import com.example.client.network.collect.CollectService
import com.example.client.network.history.HistoryService
import com.example.client.network.quiz.AnswerService
import com.example.client.network.quiz.QuestionService
import com.example.client.network.quiz.QuestionTypeService
import com.example.client.network.quiz.QuizService
import com.example.client.network.room.RoomService
import com.example.client.network.topic.TopicService
import com.example.client.network.user.UserService
import com.example.client.repositories.*
import com.example.client.utils.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
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
import java.util.concurrent.TimeUnit
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
            .add(DateJsonAdapter())
            .add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(AppConstants.APP_BASE_URL)
            .client(httpClient
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build()
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth()  = FirebaseAuth.getInstance()


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
    fun providesAuthRepository(authService: AuthService,firebaseAuth: FirebaseAuth) : AuthRepository {
        return AuthRepository(authService,firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesQuizService(retrofit: Retrofit) : QuizService {
        return retrofit.create(QuizService::class.java)
    }

    @Provides
    @Singleton
    fun providesQuizRepository(
        quizService: QuizService,
        questionTypeService: QuestionTypeService,
        questionService:QuestionService,
        answerService: AnswerService
    ) : QuizRepository {
        return QuizRepository(quizService,questionTypeService,questionService,answerService)
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

    @Provides
    @Singleton
    fun providesCollectService(retrofit: Retrofit) : CollectService {
        return retrofit.create(CollectService::class.java)
    }

    @Provides
    @Singleton
    fun providesCollectRepository(collectService: CollectService) : CollectRepository {
        return CollectRepository(collectService)
    }

    @Provides
    @Singleton
    fun providesHistoryService(retrofit: Retrofit) : HistoryService {
        return retrofit.create(HistoryService::class.java)
    }

    @Provides
    @Singleton
    fun providesHisotryRepository(historyService: HistoryService) : HistoryRepository {
        return HistoryRepository(historyService)
    }

    @Provides
    @Singleton
    fun providesRoomService(retrofit: Retrofit) : RoomService {
        return retrofit.create(RoomService::class.java)
    }

    @Provides
    @Singleton
    fun providesRoomRepository(roomService: RoomService) : RoomRepository {
        return RoomRepository(roomService)
    }


    @Provides
    @Singleton
    fun providesQuestionTypeService(retrofit: Retrofit) : QuestionTypeService {
        return retrofit.create(QuestionTypeService::class.java)
    }

    @Provides
    @Singleton
    fun providesQuestionService(retrofit: Retrofit) : QuestionService {
        return retrofit.create(QuestionService::class.java)
    }

    @Provides
    @Singleton
    fun providesAnswerService(retrofit: Retrofit) : AnswerService {
        return retrofit.create(AnswerService::class.java)
    }

}