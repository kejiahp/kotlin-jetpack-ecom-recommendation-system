package com.example.mobile.core.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.security.crypto.MasterKey
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.mobile.auth.login.LoginData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authPreferenceService: AuthPreferenceService
) : ViewModel() {

//    val authUser: LiveData<LoginData?> = authPreferenceService.authUser.asLiveData() // Convert StateFlow to LiveData
    // Expose userData as StateFlow for UI to collect
    val authUser: StateFlow<LoginData?> = authPreferenceService.authUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keeps collecting for 5 seconds after last subscriber
        initialValue = null
    )

    /**
     * Update authenticated user
     * */
    fun updateAuthUser(loginData: LoginData) {
        authPreferenceService.saveLoginResData(loginData)

    }

    /**
     * Delete authenticated user
     * */
    fun deleteAuthUser() {
        authPreferenceService.removeLoginResData()

    }
}

class AuthPreferenceService @Inject constructor(
    private val context: Context
) {
    companion object {
        const val PREFERENCE_KEY = "secure_prefs"
        const val TAG = "AuthPreferenceService"
        private const val AUTH_KEY = "auth_token"
    }

    private val _authUser: MutableStateFlow<LoginData?> = MutableStateFlow(null)
    val authUser = _authUser.asStateFlow()

    /** Listens for changes in authUser data */
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == AUTH_KEY) {
            loadToken()
        }
    }


    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCE_KEY,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // THIS `init` MUST COME AFTER everything APPARENTLY `init` IS EXECUTED IN THE SAME ORDER IT APPEARS IN A CLASS, INTERLEAVED WITH PROPERTY INITIALIZERS
    init {
        loadToken()
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    /**
     * Serialize login data as a string
     * */
    fun saveLoginResData(loginData: LoginData) {
        val loginResJson = Gson().toJson(loginData)
        sharedPreferences.edit().putString(AUTH_KEY, loginResJson).apply()

        _authUser.value = loginData
    }

    /**
     * Deletes the login data from share preferences
     * */
    fun removeLoginResData() {
        sharedPreferences.edit().remove(AUTH_KEY).apply()

        _authUser.value = null
    }

    /**
     * Deserialize login data as a `LoginData` data class
     * */
    private fun getLoginResData(): LoginData? {
        val loginData = sharedPreferences.getString(AUTH_KEY, null)
        if (loginData != null) {
            return Gson().fromJson(loginData, LoginData::class.java)
        }
        return null
    }

    /**
     * Load authenticated user on view model init
     * */
    private fun loadToken() {
        if (_authUser.value == null) {
            _authUser.value = getLoginResData() // Fetch from EncryptedSharedPreferences
        }
    }
}