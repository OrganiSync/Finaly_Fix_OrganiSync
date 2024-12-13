package com.example.organisync.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.organisync.MainActivity
import com.example.organisync.R
import com.example.organisync.databinding.ActivitySettingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        val imageModeNight = findViewById<ImageView>(R.id.ImageModeNight)
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Event -> {
                    // Navigate to EventActivity
                    val intent = Intent(this, Event::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_home -> {
                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_dashboard -> {
                    // Navigate to DashboardActivity
                    val intent = Intent(this, newsactivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        navView.selectedItemId = R.id.Setting

        binding.Imagelogout.setOnClickListener {
            // Log out and navigate to login activity
            val intent = Intent(this, activity_login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Atur status gambar sesuai dengan preferensi yang disimpan
        updateImageMode(imageModeNight, isDarkMode)

        // Mengubah tema saat gambar diklik
        imageModeNight.setOnClickListener {
            val newMode = !isDarkMode
            Log.d("SettingFragment", "Switch is checked: $newMode")
            if (newMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Log.d("SettingFragment", "Aktifkan mode gelap")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Log.d("SettingFragment", "Aktifkan mode terang")
            }
            // Simpan preferensi dark mode
            sharedPreferences.edit().putBoolean("dark_mode", newMode).apply()
            updateImageMode(imageModeNight, newMode)
        }
    }

    private fun updateImageMode(imageView: ImageView, isDarkMode: Boolean) {
        if (isDarkMode) {
            imageView.setImageResource(R.drawable.modenight)
        } else {

        }
    }
}