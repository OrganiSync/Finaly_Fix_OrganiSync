package com.example.organisync.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.organisync.MainActivity
import com.example.organisync.R
import com.example.organisync.databinding.ActivityNewsDetailBinding
import com.example.organisync.model.News
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
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
                R.id.Event -> {
                    // Navigate to NewsActivity
                    val intent = Intent(this, Event::class.java)
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
        val news = getNewsDetails()

        binding.apply {
            tvTitle.text = news.title
            tvOrganization.text = news.namaOrganisasi
            tvDescription.text = news.deskripsi
            tvUniversity.text = news.asalUniversitas
            Glide.with(this@NewsDetailActivity)
                .load(news.photo)
                .into(ivNews)
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getNewsDetails(): News {
        return News(
            id = 0,
            title = intent.getStringExtra("title").toString(),
            deskripsi = intent.getStringExtra("description").toString(),
            photo = intent.getStringExtra("photo").toString(),
            asalUniversitas = intent.getStringExtra("universitas").toString(),
            namaOrganisasi = intent.getStringExtra("organisasi").toString(),
            nama = intent.getStringExtra("nama").toString()
        )
    }
}