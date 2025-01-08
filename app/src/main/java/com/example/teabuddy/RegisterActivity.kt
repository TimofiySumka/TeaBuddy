package com.example.teabuddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.teabuddy.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.OldUserButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.RegButton.setOnClickListener{
            if (
                binding.RegEmail.text.toString().isEmpty() ||
                binding.RegPassword.text.toString().isEmpty() ||
                binding.RegRePassowrd.text.toString().isEmpty())
            {
                binding.RegText.setTextSize(19f)
                binding.RegText.text = getString(R.string.EmptFld)
            }
            else{
                if (binding.RegPassword.text.toString() == binding.RegRePassowrd.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.RegEmail.text.toString().trim(),
                        binding.RegPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = FirebaseAuth.getInstance().currentUser
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                binding.RegText.setTextSize(19f)
                                val exception = task.exception
                                if (exception != null) {
                                    when {
                                        exception is FirebaseAuthInvalidCredentialsException -> {
                                            binding.RegText.text = getString(R.string.InvEmailFormat)
                                        }
                                        exception is FirebaseAuthUserCollisionException -> {
                                            binding.RegText.text = getString(R.string.EmailRegistered)
                                        }
                                        exception is FirebaseAuthWeakPasswordException -> {
                                            binding.RegText.text = getString(R.string.WeakPswrd)
                                        }
                                        else -> {
                                            binding.RegText.text = exception.message ?: getString(R.string.BasicError)
                                        }
                                    }
                                }
                            }
                        }
                } else {
                    binding.RegText.setTextSize(19f)
                    binding.RegText.text = getString(R.string.InvRePasswrd)
                }
            }
        }
    }
}
