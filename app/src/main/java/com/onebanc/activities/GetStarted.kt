package com.onebanc.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.onebanc.R
import com.onebanc.adapter.OnboardingItemsAdapter
import com.onebanc.databinding.ActivityGetStartedBinding
import com.onebanc.model.OnboardingItem


class GetStarted : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorContainer: LinearLayout

    private val binding: ActivityGetStartedBinding by lazy {
        ActivityGetStartedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install the splash screen
        installSplashScreen()

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Set the content view with ViewBinding
        setContentView(binding.root)

        // Apply window insets listener for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set onboarding items for ViewPager2
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)

        binding.buttongs.setOnClickListener {
            val scaleAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@GetStarted, LoginActivity::class.java)
                        startActivity(intent)
                    }, 50)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.buttongs.startAnimation(scaleAnimation)
        }
    }

    // Set the list of ViewPager2 in the onboarding flow
    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.community_gs,
                    title = "Dependable Community",
                    description = "Be a part of the conversation! Engage with the community by commenting on reviews and sharing your thoughts."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.authentic_gs_2,
                    title = "Royal Reviews",
                    description = "Explore the power of user opinions! Dive into a sea of reviews and ratings submitted by our vibrant community."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.ratings_gs,
                    title = "Profound Searches",
                    description = "Find what you're looking for effortlessly. Our advanced search and filtering capabilities help you pinpoint reviews for specific products, categories, or topics."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.discount_gs,
                    title = "Hot Offers",
                    description = "Unleash a world of exclusive offers tailored just for you. Enjoy access to special promotions, discounts, and limited-time deals."
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingItemsAdapter
        onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
    }

    // Setup the indicators on the top left corner
    private fun setupIndicators() {
        indicatorContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.gs_indicator_inactive_bg
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }

    // Set the current active indicator
    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.gs_indicator_active_bg
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.gs_indicator_inactive_bg
                    )
                )
            }
        }
    }
}
