package com.example.cashiq.UI.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cashiq.UI.fragment.TodayFragment
import com.example.cashiq.UI.fragment.WeekFragment
import com.example.cashiq.UI.fragment.MonthFragment
import com.example.cashiq.UI.fragment.YearFragment

class DashPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4 // Total number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> WeekFragment()
            2 -> MonthFragment()
            3 -> YearFragment()
            else -> TodayFragment() // Default case
        }
    }
}
