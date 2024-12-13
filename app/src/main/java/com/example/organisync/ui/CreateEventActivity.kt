package com.example.organisync.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organisync.MainActivity
import com.example.organisync.R
import com.example.organisync.databinding.ActivityCreateEventBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        setupListeners()
        val navView: BottomNavigationView = binding.navView
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

    private fun setupListeners() {
        binding.cardPhoto.setOnClickListener {
            // Handle image selection
            selectImage()
        }

        binding.btnBuat.setOnClickListener {
            createEvent()
        }
    }

    private fun selectImage() {
        // Implement image selection logic
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.ivEventImage.setImageURI(selectedImageUri)
            binding.ivEventImage.visibility = View.VISIBLE // Ensure the ImageView is visible
        }
    }

    private fun createEvent() {
        val title = binding.edtJudulEvent.text.toString()
        val description = binding.edtDeskripsiEvent.text.toString()

        if (title.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val event = EventDetails(
            title = title,
            description = description,
            imageUrl = selectedImageUri.toString()
        )

        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra("event", event)
        startActivity(intent)
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    data class EventDetails(
        val title: String,
        val description: String,
        val imageUrl: String
    ) : java.io.Serializable
}