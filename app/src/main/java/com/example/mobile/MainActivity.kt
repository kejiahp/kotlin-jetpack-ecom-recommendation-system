package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile.core.navigation.NavGraph
import com.example.mobile.ui.theme.MobileTheme
import androidx.navigation.compose.rememberNavController
import com.example.mobile.auth.login.LoginData
import com.example.mobile.core.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


/**
 * The `@AndroidEntryPoint` annotation ensures the main activity knows it is a dagger hilt application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels() // initially auth view model
//    private val authUser: MutableStateFlow<LoginData?> = MutableStateFlow(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                authViewModel.authUser.collect { authU ->
//                    authUser.value = authU
//                }
//            }
//        }


        enableEdgeToEdge()
        setContent {
            MobileTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        navController = navController,
                    )
                }
            }
        }
    }
}