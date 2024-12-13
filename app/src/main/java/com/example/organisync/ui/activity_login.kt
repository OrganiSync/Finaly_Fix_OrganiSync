package com.example.organisync.ui

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.organisync.MainActivity
import com.example.organisync.R
import com.example.organisync.User.UserPreferences
import com.example.organisync.ViewModel.ViewModelFactory
import com.example.organisync.ViewModel.ViewModelLogin
import com.example.organisync.databinding.ActivityLoginBinding

class activity_login : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: ViewModelLogin

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[ViewModelLogin::class.java]

        initializeUI()
    }

    private fun initializeUI() {
        playAnimation()
        setUpAction()
        observeLogin()
    }

    private fun setUpAction() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailInput.text
            val password = binding.passwordInput.text
            when {
                email.isNullOrEmpty() -> {
                    Toast.makeText(this, "Harap isi email anda", Toast.LENGTH_SHORT).show()
                }
                password.isNullOrEmpty() -> {
                    Toast.makeText(this, "Harap isi password anda", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setUpLogin()
                    showLoading(true)
                }
            }
        }
        binding.signUpText.setOnClickListener {
            val intent = Intent(this@activity_login, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.face.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Handle the image captured
        }
    }

    private fun setUpLogin() {
        binding.apply {
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()
            loginViewModel.login(email, pass)
        }
    }

    private fun observeLogin() {
        loginViewModel.getLogin().observe(this) {
            if (it.error) {
                showLoading(false)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else {
                showLoading(true)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@activity_login, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginImage = ObjectAnimator.ofFloat(binding.imageLogin, View.ALPHA, 1f).setDuration(500)
        val txtLogin = ObjectAnimator.ofFloat(binding.titleText, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.signInButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.signUpText, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playSequentially(loginImage, txtLogin, emailEditText, passwordEditTextLayout, login, register)
        }
        together.start()
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}