package com.example.cashiq.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.fragment.BudgetFragment
import com.example.cashiq.UI.fragment.DashFragment
import com.example.cashiq.UI.fragment.NoteFragment
import com.example.cashiq.UI.fragment.ProfileFragment
import com.example.cashiq.UI.fragment.TransactionFragment
import com.example.cashiq.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display DashFragment by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.dashframe, DashFragment())
                .commit()
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.bottomNav.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navHome -> {
                        // Navigate to DashFragment
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.dashframe, DashFragment())
                            .commit()
                        true
                    }
                    R.id.NavProfile -> {
                        // Navigate to ProfileFragment
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.dashframe, ProfileFragment())
                            .commit()
                        true
                    }
                    R.id.NavBudget -> {
                        // Navigate to ProfileFragment
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.dashframe, BudgetFragment())
                            .commit()
                        true
                    }R.id.NavTransaction -> {
                    // Navigate to ProfileFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.dashframe, TransactionFragment())
                        .commit()
                    true
                }R.id.NavAdd -> {
                    // Navigate to ProfileFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.dashframe, NoteFragment())
                        .commit()
                    true
                }
                    else -> false
                }
            }
            selectedItemId = R.id.navHome // Set the default selected item
        }
    }
}
