package com.athallah.ecommerce

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.athallah.ecommerce.databinding.ActivityMainBinding
import com.athallah.ecommerce.utils.Helper
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val remoteConfig: FirebaseRemoteConfig by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        checkAppLanguage()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        observeLoginSession()
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun observeLoginSession() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.checkUserLogin().distinctUntilChanged().collect { isAuthorize ->
                    if (!isAuthorize) {
                        viewModel.logout()
                        navController.navigate(R.id.action_global_prelogin_navigation)
                    }
                }
            }
        }
    }

    private fun checkAppLanguage() {
        runBlocking {
            val appLanguage = viewModel.getAppLanguage().first()
            Helper.setAppLanguage(appLanguage)
        }
    }

    fun logout() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_global_prelogin_navigation)
    }
}
