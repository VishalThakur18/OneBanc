package com.onebanc.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.onebanc.R
import com.onebanc.adapter.RegisterActivityAdapter
import com.google.android.material.tabs.TabLayoutMediator

class RegisterActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Set up the TabLayout and ViewPager2
        tabLayout = findViewById(R.id.tablout)
        viewPager2 = findViewById(R.id.pager_feed)

        // Set the adapter for the ViewPager2
        viewPager2.adapter = RegisterActivityAdapter(this)

        // Set up the TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Account Number"
                1 -> tab.text = "Cards"
            }
        }.attach()

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
