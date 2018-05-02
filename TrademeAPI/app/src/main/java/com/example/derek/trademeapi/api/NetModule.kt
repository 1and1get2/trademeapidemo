package com.example.derek.trademeapi.api

import com.example.derek.trademeapi.BuildConfig
import com.example.derek.trademeapi.api.interceptors.AuthenticationHeaderInterceptor
import com.example.derek.trademeapi.api.moshiadapters.CategoryAdapter
import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateJsonAdapter
import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateTime
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by derek on 30/04/18.
 */

@Module
abstract class NetModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.d(it) })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            return OkHttpClient.Builder()
                    .addInterceptor(AuthenticationHeaderInterceptor())
//                    .addInterceptor(loggingInterceptor)//.also { it.networkInterceptors().add(StethoInterceptor()) }
                    .build()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideMoshi(): Moshi =
                Moshi.Builder()
                        .add(CategoryAdapter())
//                        .add(Category::class.java, CategoryAdapter())
//                    .add(RealmListAdapter.FACTORY)
                        .add(TradeMeDateTime::class.java, TradeMeDateJsonAdapter())
                        .add(KotlinJsonAdapterFactory())
                        .build()

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
                Retrofit.Builder()
                        .client(okHttpClient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .baseUrl(BuildConfig.BASE_URI)
                        .build()

        @JvmStatic
        @Provides
        @Singleton
        fun provideTradeMeApiService(retrofit: Retrofit): TradeMeApiService = retrofit.create(TradeMeApiService::class.java)
    }
}

