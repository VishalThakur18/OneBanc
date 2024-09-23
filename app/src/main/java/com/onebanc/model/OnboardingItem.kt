package com.onebanc.model

data class OnboardingItem(
    val onboardingImage: Int,
    val title: CharSequence,  // Changed from String to CharSequence
    val description: String
)

