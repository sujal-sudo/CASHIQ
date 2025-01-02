package com.example.cashiq.UI.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cashiq.R
import com.example.cashiq.UI.fragment.HomeFragment
import com.example.cashiq.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.IconHolder.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> replaceFragment(HomeFragment())
//                R.id.NavProfile -> replaceFragment(UserFragment())
//                R.id.navNoti -> replaceFragment(NotificationFragment())

                else -> {}
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(binding.IconHolder.id, fragment)
        fragmentTransaction.commit()
    }
}