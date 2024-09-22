package com.onebanc.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onebanc.fragment.AccountFragment
import com.onebanc.fragment.CardsFragment
import com.onebanc.fragment.InvestmentFragment

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3 // Total number of fragments (Account, Cards, Investment)
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AccountFragment() // Replace with your actual fragment classes
            1 -> CardsFragment()
            2 -> InvestmentFragment()
            else -> AccountFragment()
        }
    }
}
