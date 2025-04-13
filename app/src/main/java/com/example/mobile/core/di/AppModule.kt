package com.example.mobile.core.di

import android.content.Context
import com.example.mobile.auth.codereset.CodeResetApiService
import com.example.mobile.auth.codereset.CodeResetDataSource
import com.example.mobile.auth.codereset.CodeResetDataSourceInterface
import com.example.mobile.auth.codereset.CodeResetRepository
import com.example.mobile.auth.login.LoginApiService
import com.example.mobile.auth.login.LoginDataSource
import com.example.mobile.auth.login.LoginDataSourceInterface
import com.example.mobile.auth.login.LoginRepository
import com.example.mobile.auth.signup.SignUpApiService
import com.example.mobile.auth.signup.SignUpDataSource
import com.example.mobile.auth.signup.SignUpDataSourceImpl
import com.example.mobile.auth.signup.SignUpRepository
import com.example.mobile.core.CoreConstants
import com.example.mobile.core.auth.AuthDeletePreferenceOnBadToken
import com.example.mobile.core.auth.AuthInterceptor
import com.example.mobile.core.auth.AuthPreferenceService
import com.example.mobile.product_cart_order.ProdCartOrderSharedPreferenceService
import com.example.mobile.product_cart_order.ProductCartOrderApiService
import com.example.mobile.product_cart_order.ProductCartOrderDataSource
import com.example.mobile.product_cart_order.ProductCartOrderDataSourceInterface
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
     * AuthPreferenceService dependency definition
     *
     *
     * This will pass the application context to the authPreference service, without leaking memory
     * */
    @Provides
    @Singleton
    fun providesAuthPreferenceService(@ApplicationContext context: Context): AuthPreferenceService {
        return AuthPreferenceService(context = context)
    }

    /**
     * ProdCartOrderSharedPreferenceService dependency definition
     * */
    @Provides
    @Singleton
    fun providesProdCartOrderSharedPreferenceService(@ApplicationContext context: Context): ProdCartOrderSharedPreferenceService {
        return ProdCartOrderSharedPreferenceService(context = context)
    }

    /**
     * Retrofit object definition
     *
     * The `@Provides` annotation creates provider method binding.
     * The `@Singleton` annotation ensures the dependency is only instantiated once
     * */
    @Provides
    @Singleton
    fun providesRetrofit(authPreferenceService: AuthPreferenceService): Retrofit {
        // To see api calls and responses within LogCat
        // the `apply` scope function allows the configuration of an object within a block and return the object itself
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            // specify the logs to see, `BASIC` will display logs like URl, Methods etc.
            level = HttpLoggingInterceptor.Level.BASIC
        }
        // Defining an httpclient to be used together with retrofit
        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
            // add `Authorization` header
            addInterceptor(AuthInterceptor(authPreferenceService.authUser.value?.tkn))
            // if status code 403, delete auth token
            addInterceptor(AuthDeletePreferenceOnBadToken(authPreferenceService::removeLoginResData))
        }
        // Timeout each request in 120 seconds
        httpClient.readTimeout(120, TimeUnit.SECONDS)

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

    /** LoginApiService dependency definition
     *
     * This allows the use of `LoginApiService` through dependency injection
     * */
    @Provides
    @Singleton
    fun providesLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    /** LoginDataSource dependency definition
     *
     * This allows the use of `LoginDataSource` through dependency injection
     * */
    @Provides
    @Singleton
    fun providesLoginDataSource(loginApiService: LoginApiService): LoginDataSourceInterface {
        return LoginDataSource(loginApiService)
    }

    /** LoginRepository dependency definition
     *
     * This allows the use of `LoginRepository` through dependency injection
     * */
    @Provides
    @Singleton
    fun providesLoginRepository(loginDataSource: LoginDataSource): LoginRepository {
        return LoginRepository(loginDataSource)
    }

    /**
     * CodeResetApiService dependency definition
     * */
    @Provides
    @Singleton
    fun providesCodeResetApiService(retrofit: Retrofit): CodeResetApiService {
        return retrofit.create(CodeResetApiService::class.java)
    }

    /**
     * CodeResetDataSource dependency definition
     * */
    @Provides
    @Singleton
    fun providesCodeResetDataSource(codeResetApiService: CodeResetApiService): CodeResetDataSourceInterface {
        return CodeResetDataSource(codeResetApiService)
    }

    /**
     * CodeResetRepository dependency definition
     * */
    @Provides
    @Singleton
    fun providesCodeResetRepository(codeResetDataSource: CodeResetDataSource): CodeResetRepository {
        return CodeResetRepository(codeResetDataSource)
    }

    /**
     * ProductCartOrderApiService dependency definition
     * */
    @Provides
    @Singleton
    fun providesProductCartOrderApiService(retrofit: Retrofit): ProductCartOrderApiService {
        return retrofit.create(ProductCartOrderApiService::class.java)
    }

    /**
     * ProductCartOrderApiService dependency definition
     * */
    @Provides
    @Singleton
    fun providesProductCartOrderDataSource(productCartOrderApiService: ProductCartOrderApiService): ProductCartOrderDataSourceInterface {
        return ProductCartOrderDataSource(productCartOrderApiService)
    }

    /**
     * ProductCartOrderDataSource dependency definition
     * */
    @Provides
    @Singleton
    fun providesProductCartOrderRepository(productCartOrderDataSource: ProductCartOrderDataSource): ProductCartOrderRepository {
        return ProductCartOrderRepository(productCartOrderDataSource)
    }
}