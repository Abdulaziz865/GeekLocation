package com.example.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.presentation.R
import com.example.presentation.utils.SharedPreferenceUtil

class MainActivity : AppCompatActivity() {

    private lateinit var controllerNav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GeekLocation)
        Thread.sleep(2000)
        setContentView(R.layout.activity_main)
        checkUserState()
    }

    private fun checkUserState() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        controllerNav = navHostFragment.navController

        when (SharedPreferenceUtil.isPreference) {
            true -> {
                controllerNav.navigate(R.id.googleMapFragment)
            }
            else -> {
                controllerNav.navigate(R.id.signInFragment)
            }
        }
    }
}