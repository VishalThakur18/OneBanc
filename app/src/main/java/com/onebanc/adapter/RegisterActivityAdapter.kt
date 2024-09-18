package com.onebanc.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onebanc.fragment.RegisterWithAccountNumber
import com.onebanc.fragment.RegisterWithCards
import com.onebanc.activities.RegisterActivity

class RegisterActivityAdapter(activity: RegisterActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisterWithAccountNumber()
            1 -> RegisterWithCards()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
