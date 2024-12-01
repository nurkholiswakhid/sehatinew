package com.bangkit.sehati.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.sehati.databinding.SignupActivityBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: SignupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // Pastikan FirebaseAuth sudah diinisialisasi

        binding.signUpButton.setOnClickListener {
            val firstname = binding.firstNameInput.text.toString()
            val lastname = binding.lastNameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (checkAllField()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.signOut()
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("error:", task.exception.toString())
                        Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAllField(): Boolean {
        val email = binding.emailInput.text?.toString() ?: ""

        if (binding.firstNameInput.text?.toString().isNullOrEmpty()) {
            binding.firstNameLayout.error = "This is a required field"
            return false
        }

        if (binding.lastNameInput.text?.toString().isNullOrEmpty()) {
            binding.lastNameLayout.error = "This is a required field"
            return false
        }

        if (email.isEmpty()) {
            binding.emailLayout.error = "This is a required field"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Check email format"
            return false
        }

        if (binding.passwordInput.text?.toString().isNullOrEmpty()) {
            binding.passwordLayout.error = "This is a required field"
            return false
        }

        if (binding.passwordInput.text!!.length <= 8) {
            binding.passwordLayout.error = "Password must be at least 8 characters"
            return false
        }

        return true
    }
}
