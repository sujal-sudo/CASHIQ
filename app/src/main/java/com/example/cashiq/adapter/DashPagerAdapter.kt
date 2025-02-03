package com.example.cashiq.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cashiq.UI.fragment.MonthFragment
import com.example.cashiq.UI.fragment.NoInternetFragment
import com.example.cashiq.UI.fragment.TodayFragment
import com.example.cashiq.UI.fragment.WeekFragment
import com.example.cashiq.UI.fragment.YearFragment
import com.example.cashiq.utils.isNetworkAvailable

class DashPagerAdapter(fragment: Fragment, context: Context) : FragmentStateAdapter(fragment) {
    private val fragmentList: List<Fragment>

    init {
        // Check internet connection and set the fragment list accordingly
        fragmentList = if (isNetworkAvailable(context)) {
            listOf(
                TodayFragment(),
                WeekFragment(),
                MonthFragment(),
                YearFragment()
            )
        } else {
            listOf(NoInternetFragment()) // Only show NoInternetFragment if no connection
        }
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}