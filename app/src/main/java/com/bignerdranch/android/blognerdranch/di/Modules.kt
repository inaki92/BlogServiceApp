package com.bignerdranch.android.blognerdranch.di

import android.content.Context
import android.net.ConnectivityManager
import com.bignerdranch.android.blognerdranch.rest.BlogService
import com.bignerdranch.android.blognerdranch.rest.CacheInterceptor
import com.bignerdranch.android.blognerdranch.rest.PostRepository
import com.bignerdranch.android.blognerdranch.rest.PostRepositoryImpl
import com.bignerdranch.android.blognerdranch.utils.BASE_URL
import com.bignerdranch.android.blognerdranch.utils.cacheSize
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideApiService(retrofit: Retrofit): BlogService =
        retrofit.create(BlogService::class.java)

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    fun providesGson(): Gson = Gson()

    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cacheInterceptor: CacheInterceptor,
        cache: Cache
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(cacheInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun providesCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, cacheSize)
}

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesConnectivityManager(
        @ApplicationContext app: Context
    ): ConnectivityManager =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideRepository(repo: PostRepositoryImpl): PostRepository
}