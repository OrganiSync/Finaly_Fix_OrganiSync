package com.example.organisync.ui

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.organisync.R
import com.example.organisync.ViewModel.ViewModelRegister
import com.example.organisync.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_IMAGE_CAPTURE = 101
    }

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var registerViewModel: ViewModelRegister

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ViewModelRegister::class.java]

        initializeUI()
    }

    private fun initializeUI() {
        observeRegister()
        setUpAction()
        showLoading(false)
        playAnimation()
    }

    private fun observeRegister() {
        registerViewModel.getRegister().observe(this) {
            if (it == null) {
                showLoading(true)
            } else {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
                val intent = Intent(this@SignUpActivity, activity_login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation() {

        val txtRegister = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val txtWelcome = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameInput, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 1f).setDuration(500)
        val face = ObjectAnimator.ofFloat(binding.AddYourFace, View.ALPHA, 1f).setDuration(500)
        val TombolCreate = ObjectAnimator.ofFloat(binding.CreateAccount, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(txtRegister, name, emailEditText, passwordEditTextLayout,TombolCreate, face, txtWelcome)
            startDelay = 500
        }.start()
    }

    private fun setUpAction() {
        binding.CreateAccount.setOnClickListener {
            val nama = binding.nameInput.text
            val email = binding.emailInput.text
            val password = binding.passwordInput.text
            if (nama != null) {
                when {
                    nama.isEmpty() -> {
                        Toast.makeText(this, "Harap isi nama anda", Toast.LENGTH_SHORT).show()
                    }

                    email.isNullOrEmpty() -> {
                        Toast.makeText(this, "Harap isi email anda", Toast.LENGTH_SHORT).show()
                    }

                    password.isNullOrEmpty() -> {
                        Toast.makeText(this, "Isi password anda dengan benar", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        setUpRegister()
                        showLoading(true)
                    }
                }
            }
        }
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, activity_login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.AddYourFace.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Handle the imageBitmap (e.g., display it in an ImageView or save it)
        }
    }

    private fun setUpRegister() {
        binding.apply {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()

            registerViewModel.register(name, email, pass)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}