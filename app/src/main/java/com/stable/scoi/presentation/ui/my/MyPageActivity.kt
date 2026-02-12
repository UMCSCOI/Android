package com.stable.scoi.presentation.ui.my

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stable.scoi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.myPageFragment,
                R.id.accountInfoFragment,
                R.id.apiSettingFragment,
                R.id.apiEditFragment,
                R.id.changePasswordFragment -> {
                    //bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    //bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}