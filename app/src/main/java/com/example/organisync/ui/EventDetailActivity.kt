package com.example.organisync.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.organisync.MainActivity
import com.example.organisync.R
import com.example.organisync.databinding.ActivityEventDetailBinding
import com.example.organisync.model.News
import com.google.android.material.bottomnavigation.BottomNavigationView

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        setupViews()
        setupBackButton()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_dashboard -> {
                    // Navigate to NewsActivity
                    val intent = Intent(this, newsactivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Setting -> {
                    // Navigate to Setting
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViews() {
        // Assuming you have an Event object with the details
        val event = getEventDetails()

        binding.apply {
            tvEventTitle.text = event.title
            tvEventDescription.text = event.deskripsi
            tvUniversity.text = event.asalUniversitas
            tvUserName.text = event.nama

            Glide.with(this@EventDetailActivity)
                .load(event.photo)
                .into(ivEventImage)
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
    private fun getEventDetails(): News {
        // Replace this with actual event details retrieval logic
        return News(
            id = 0,
            title = intent.getStringExtra("title").toString(),
            deskripsi = intent.getStringExtra("description").toString(),
            photo = intent.getStringExtra("photo").toString(),
            asalUniversitas = intent.getStringExtra("universitas").toString(),
            namaOrganisasi = intent.getStringExtra("organisasi").toString(),
            nama = intent.getStringExtra("nama").toString(
        ))
    }

    data class EventDetails(
        val title: String,
        val description: String,
        val imageUrl: String
    )

}