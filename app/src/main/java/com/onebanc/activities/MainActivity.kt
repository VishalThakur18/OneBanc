package com.onebanc.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.onebanc.R
import com.onebanc.databinding.ActivityMainBinding
import com.onebanc.fragment.CardsFragment
import com.onebanc.fragment.HomeFragment
import com.onebanc.fragment.RewardsFragment
import com.onebanc.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //QR
    private lateinit var scanQrBtn: FloatingActionButton

    private var isScannerInstalled = false
    private lateinit var scanner: GmsBarcodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Set navigation bar color to white
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        // Adjust status and navigation bar icon colors
        setStatusAndNavBarColors()

        // Inflate the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set fragment switching on button clicks
        binding.homeViewBtn.setOnClickListener {
            replaceFragment(HomeFragment())
        }

        binding.cardViewBtn.setOnClickListener {
            replaceFragment(CardsFragment())
        }

        binding.offerViewBtn.setOnClickListener {
            replaceFragment(RewardsFragment())
        }

        binding.settingViewBtn.setOnClickListener {
            replaceFragment(SettingsFragment())
        }

        // Apply edge-to-edge padding for system bars
        applyEdgeToEdge()
        // Load the default fragment (HomeFragment) on first launch
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
        //QR
        installGoogleScanner()
        initVars()
        registerUiListener()
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    // Function to adjust the status bar and navigation bar colors
    private fun setStatusAndNavBarColors() {
        // Set the status bar to light mode (dark icons)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Set the navigation bar to light mode (dark icons)
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    // Apply padding to handle system bars (status and navigation)
    private fun applyEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    //QR
    private fun installGoogleScanner() {
        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()

        moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener {
            isScannerInstalled = true
        }.addOnFailureListener {
            isScannerInstalled = false
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initVars() {
        scanQrBtn = findViewById(R.id.scanQrBtn)
        val options = initializeGoogleScanner()
        scanner = GmsBarcodeScanning.getClient(this, options)
    }

    private fun initializeGoogleScanner(): GmsBarcodeScannerOptions {
        return GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom().build()
    }

    private fun registerUiListener() {
        scanQrBtn.setOnClickListener {
            if (isScannerInstalled) {
                startScanning()
            } else {
                Toast.makeText(this, "Please try again...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startScanning() {
        scanner.startScan().addOnSuccessListener {
            val result = it.rawValue
            result?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }.addOnCanceledListener {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }
    }

}
