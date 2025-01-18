package com.example.teabuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.teabuddy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.NewUserButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.LogButton.setOnClickListener{
            if (binding.LogEmail.text.toString().isEmpty() || binding.LogPassword.text.toString().isEmpty()) {
                binding.logText.text = getString(R.string.EmptFld)
                binding.logText.setTextSize(19f)
            }
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.LogEmail.text.toString(),
                    binding.LogPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val exception = task.exception
                        binding.logText.setTextSize(19f)
                        if (exception != null) {
                            when {
                                exception is FirebaseAuthInvalidUserException -> {
                                    binding.logText.text = getString(R.string.InvEmail)
                                }
                                exception is FirebaseAuthInvalidCredentialsException -> {
                                    binding.logText.text = getString(R.string.InvPasswrd)
                                }
                                else -> {
                                    binding.logText.text = getString(R.string.BasicError)
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

