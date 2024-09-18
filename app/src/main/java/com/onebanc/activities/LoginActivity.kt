package com.onebanc.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.onebanc.databinding.ActivityLoginBinding
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }



    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor

    private var failedAttempts = 0
    private val maxFailedAttempts = 3
    private var isScannerEnabled = true // Tracks if the scanner is enabled or temporarily disabled
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Text to be shown in the TextView
        val fullText = "Don't have an Account? Register"
        val spannableString = SpannableString(fullText)

        // Highlight the word "Register" with blue color
        val startIndex = fullText.indexOf("Register")
        val endIndex = startIndex + "Register".length

        spannableString.setSpan(
            ForegroundColorSpan(Color.BLUE), // Set the color to blue
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the SpannableString to the TextView
        binding.signUp.text = spannableString

        handler = Handler(Looper.getMainLooper()) // For delaying the reactivation of the scanner

        setupBiometricAuthentication()

        binding.signUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Trigger biometric authentication when the fingerprint icon is clicked
        binding.fingerScan.setOnClickListener {
            if (isScannerEnabled) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                // Optionally, show a message indicating the scanner is temporarily disabled
            }
        }
    }

    private fun setupBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle error, like too many failed attempts
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Reset the failed attempts counter
                failedAttempts = 0
                // Proceed to the MainActivity
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                vibratePhone() // Vibrate on failed attempt
                failedAttempts++

                if (failedAttempts >= maxFailedAttempts) {
                    disableScannerTemporarily() // Disable the scanner for a few seconds
                }
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for OneBanc")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        checkBiometricAvailability()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(500) // Vibrates for 500 milliseconds
    }

    private fun disableScannerTemporarily() {
        isScannerEnabled = false
        // Disable the fingerprint scanner and show a message
        binding.fingerScan.isEnabled = false

        // Re-enable the scanner after 10 seconds
        handler.postDelayed({
            isScannerEnabled = true
            binding.fingerScan.isEnabled = true
            failedAttempts = 0 // Reset failed attempts
        }, 10000) // 10,000 milliseconds = 10 seconds
    }

    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Biometric authentication is available
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // No biometric hardware available on this device
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Biometric hardware is currently unavailable
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // The user hasn't enrolled any biometric credentials
            }
        }
    }
}
