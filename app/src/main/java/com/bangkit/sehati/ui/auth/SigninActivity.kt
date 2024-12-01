package com.bangkit.sehati.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.sehati.HomeActivity
import com.bangkit.sehati.databinding.SigninActivityBinding
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: SigninActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SigninActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Tombol Login
        binding.loginButton.setOnClickListener {
            val email = binding.emailInputLogin.text?.toString()?.trim() ?: ""
            val password = binding.passwordInputLogin.text?.toString()?.trim() ?: ""

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        // Redirect ke halaman Signup
        binding.signupText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Teks "Lupa Password"
        binding.forgotPassword.setOnClickListener {
            // Mengarahkan ke ForgotActivity
            startActivity(Intent(this, ForgotActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailLayoutLogin.error = "Email tidak boleh kosong"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayoutLogin.error = "Format email salah"
            return false
        }

        if (password.isEmpty()) {
            binding.passwordLayoutLogin.error = "Password tidak boleh kosong"
            return false
        }

        // Reset error jika validasi berhasil
        binding.emailLayoutLogin.error = null
        binding.passwordLayoutLogin.error = null
        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // Lanjutkan ke halaman berikutnya (contoh: Home_Activity)
                    startActivity(Intent(this, HomeActivity ::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
