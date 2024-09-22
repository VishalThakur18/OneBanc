package com.onebanc.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.onebanc.R
import androidx.core.content.res.ResourcesCompat
import com.onebanc.adapter.HomeViewPagerAdapter
import com.onebanc.adapter.QuickSendAdapter
import com.onebanc.databinding.FragmentHomeBinding
import com.onebanc.model.QuickSender
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var accountBlock: TextView
    private lateinit var quickSendAdapter: QuickSendAdapter
    private lateinit var cardsBlock: TextView
    private lateinit var quickRecyclerView: RecyclerView
    private lateinit var investmentBlock: TextView
    private val manropeSemiBold = context?.let { ResourcesCompat.getFont(it, R.font.manropesemibold) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views from binding
        viewPager = _binding!!.viewPagerHome
        dotsIndicator = _binding!!.dotsIndicator
        accountBlock = _binding!!.mainAccount
        cardsBlock = _binding!!.cardsBlock
        investmentBlock = _binding!!.investmentBlock
        quickRecyclerView = _binding!!.homeQuickSendRecycler

        // Set up ViewPager
        viewPagerAdapter = HomeViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // Click listeners to switch ViewPager fragments and highlight the text
        accountBlock.setOnClickListener {
            viewPager.currentItem = 0
            highlightSelectedText(0)
        }

        cardsBlock.setOnClickListener {
            viewPager.currentItem = 1
            highlightSelectedText(1)
        }

        investmentBlock.setOnClickListener {
            viewPager.currentItem = 2
            highlightSelectedText(2)
        }

        // ViewPager page change callback to update text highlight when swiping
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                highlightSelectedText(position)
            }
        })
        dotsIndicator.setViewPager2(viewPager)

        // Initialize RecyclerView and its Adapter
        quickRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val quickSenders = listOf(
            QuickSender("Sender 1", R.drawable.profile_avatar),
            QuickSender("Sender 2", R.drawable.profile_avatar),
            QuickSender("Sender 3", R.drawable.profile_avatar),
            QuickSender("Sender 4", R.drawable.profile_avatar) ,
            QuickSender("Sender 1", R.drawable.profile_avatar),
            QuickSender("Sender 2", R.drawable.profile_avatar),
            QuickSender("Sender 3", R.drawable.profile_avatar),
            QuickSender("Sender 4", R.drawable.profile_avatar)
        )

        // Initialize the adapter with the list
        quickSendAdapter = QuickSendAdapter(requireContext(), quickSenders)

        // Set the adapter to the RecyclerView
        quickRecyclerView.adapter = quickSendAdapter
    }

    // Reset the color of all blocks and highlight the selected one
    private fun highlightSelectedText(position: Int) {
        resetTextColors()

// First reset all blocks to normal styling
        accountBlock.setTextColor(Color.GRAY)  // Reset Account
        accountBlock.setTextSize(14f)           // Reset text size
        accountBlock.setTypeface(manropeSemiBold, Typeface.NORMAL)// Reset to normal

        cardsBlock.setTextColor(Color.GRAY)    // Reset Cards
        cardsBlock.setTextSize(14f)             // Reset text size
        cardsBlock.setTypeface(manropeSemiBold, Typeface.NORMAL) // Reset to normal

        investmentBlock.setTextColor(Color.GRAY) // Reset Investment
        investmentBlock.setTextSize(14f)          // Reset text size
        investmentBlock.setTypeface(manropeSemiBold, Typeface.NORMAL)  // Reset to normal

// Apply the selected styling based on position
        when (position) {
            0 -> {
                accountBlock.setTextColor(Color.BLACK)  // Highlight Account
                accountBlock.setTextSize(18f)           // Set text size
                accountBlock.setTypeface(manropeSemiBold, Typeface.BOLD)  // Make text bold
            }
            1 -> {
                cardsBlock.setTextColor(Color.BLACK)    // Highlight Cards
                cardsBlock.setTextSize(18f)             // Set text size
                cardsBlock.setTypeface(manropeSemiBold, Typeface.BOLD)  // Make text bold
            }
            2 -> {
                investmentBlock.setTextColor(Color.BLACK) // Highlight Investment
                investmentBlock.setTextSize(18f)          // Set text size
                investmentBlock.setTypeface(manropeSemiBold, Typeface.BOLD)  // Make text bold
            }
        }


    }

    // Reset the text color for all blocks to the default
    private fun resetTextColors() {
        accountBlock.setTextColor(Color.GRAY)  // Reset Account to gray
        cardsBlock.setTextColor(Color.GRAY)    // Reset Cards to gray
        investmentBlock.setTextColor(Color.GRAY) // Reset Investment to gray
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
