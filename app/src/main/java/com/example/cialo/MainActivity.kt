package com.example.cialo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.example.cialo.databinding.ActivityMainBinding
import com.example.cialo.exceptionHandling.AppError
import com.example.cialo.exceptionHandling.AppErrorType
import com.example.cialo.exceptionHandling.EstimotePermissionsError
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var _viewModel: MainViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        runEstimote();

    }

    private fun runEstimote() {
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
            this,
            {
                _viewModel.initServices()
            },
            { missing -> _viewModel.onError(EstimotePermissionsError(missing)) },
            { error -> _viewModel.onError(AppError(AppErrorType.EstimoteInitialization, "", error)) })
    }
}