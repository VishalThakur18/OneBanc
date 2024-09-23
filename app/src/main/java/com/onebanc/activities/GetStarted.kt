package com.onebanc.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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

    // Function to create Spannable with two different colors for titles
    private fun getColoredTitle(title: String, firstWordLength: Int, color1: Int, color2: Int): SpannableString {
        val spannableTitle = SpannableString(title)

        // Apply the first color
        spannableTitle.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, color1)),
            0,
            firstWordLength,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply the second color
        spannableTitle.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, color2)),
            firstWordLength + 1,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableTitle
    }

    // Set the list of ViewPager2 in the onboarding flow
    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.enhance_digitization,
                    title = getColoredTitle("Enhanced Digitization", 8, R.color.primary_color_2, R.color.black),
                    description = "Your digital account comes with all you need to spend, save and keep track of your money. Open your account digitally in minutes from our intuitive app."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.bottom_bar_personalized,
                    title = getColoredTitle("Personalized Banking", 12, R.color.text_per, R.color.black),
                    description = "By solving your problems, treating you fairly & being transparent, we can make banking better. Life becomes more fun when financial needs are smartly tuned to your lifestyle"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.bottom_bar_wellness,
                    title = getColoredTitle("Financial Wellness", 9, R.color.text_well, R.color.black),
                    description = "We believe only one person can have control over your financial life: You. Get a holistic view of all your finances in one place with AI enabled insights & recommendations."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.maximum_security_caricature_v2,
                    title = getColoredTitle("Maximum Security", 7, R.color.text_security, R.color.black),
                    description = "We know security is your top priority and we will always place it first. We are trusted by the biggest banks regulated by RBI. We are GDPR, DPDP, SOC 2 Type II, ISO, PCI DSS v4.0 and Cert-In compliant."
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
