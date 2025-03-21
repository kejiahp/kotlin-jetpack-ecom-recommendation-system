package com.example.mobile.core.di

import android.util.Log
import com.example.mobile.auth.signup.SignUpApiService
import com.example.mobile.auth.signup.SignUpDataSource
import com.example.mobile.auth.signup.SignUpDataSourceImpl
import com.example.mobile.auth.signup.SignUpRepository
import com.example.mobile.core.CoreConstants
import com.example.mobile.core.utilites.CoreUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/***
 * The `@Module` annotation allows the definition of third party dependencies with the class
 *
 * `@InstallIn(SingletonComponent::class)` ensures all the dependencies defined in the module are accessible within the whole project
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Retrofit object definition
     *
     * The `@Provides` annotation creates provider method binding.
     * The `@Singleton` annotation ensures the dependency is only instantiated once
     * */
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        // To see api calls and responses within LogCat
        // the `apply` scope function allows the configuration of an object within a block and return the object itself
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            // specify the logs to see, `BASIC` will display logs like URl, Methods etc.
            level = HttpLoggingInterceptor.Level.BASIC
        }
        // Defining an httpclient to be used together with retrofit
        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
        }
        // Timeout each request in 120 seconds
        httpClient.readTimeout(120, TimeUnit.SECONDS)

        // Defining the moshi adapter
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // Return the Retrofit object
        // Moshi didn't work so I fell back to GsonConverter
//        return Retrofit.Builder().baseUrl(CoreConstants.BASE_URL).client(httpClient.build())
//            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

        return Retrofit.Builder().baseUrl(CoreConstants.BASE_URL).client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    /**
     * SignUpApiService dependency definition
     *
     * This allows the use of `SignUpApiService` through dependency injection
     * */
    @Provides
    @Singleton
    fun providesSignUpApiService(retrofit: Retrofit): SignUpApiService {
        return retrofit.create(SignUpApiService::class.java)
    }

    /***
     * SignUpDataSource *implementation dependency definition
     *
     * This allows the use of `SignUpDataSource` *implementations through dependency injection
     */
    @Provides
    @Singleton
    fun providesSignUpDataSource(signUpApiService: SignUpApiService): SignUpDataSource {
        return SignUpDataSourceImpl(signUpApiService)
    }

    /**
     * SignUpRepository dependency definition
     *
     * This allows the use of `SignUpRepository` through dependency injection
     * */
    @Provides
    @Singleton
    fun providesSignUpRepository(signUpDataSource: SignUpDataSource): SignUpRepository {
        return SignUpRepository(signUpDataSource)
    }
}