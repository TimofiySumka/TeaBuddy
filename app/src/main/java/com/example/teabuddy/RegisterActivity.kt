package com.example.teabuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.teabuddy.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.OldUserButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.RegButton.setOnClickListener{
            binding.RegText.setTextSize(19f)
            if (
                binding.RegEmail.text.toString().isEmpty() ||
                binding.RegPassword.text.toString().isEmpty() ||
                binding.RegRePassowrd.text.toString().isEmpty() ||
                binding.RegName.text.toString().isEmpty())
            {
                binding.RegText.text = getString(R.string.EmptFld)
            } else {
                if (binding.RegPassword.text.toString().length < 6) {
                    binding.RegText.text = getString(R.string.IncrShortPswrd)
                } else if (binding.RegPassword.text.toString() == binding.RegRePassowrd.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.RegEmail.text.toString().trim(),
                        binding.RegPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = FirebaseAuth.getInstance().currentUser
                                val userId = firebaseUser?.uid
                                val normalizedUID = UUID.randomUUID().toString()
                                val user = hashMapOf(
                                    "name" to binding.RegName.text.toString(),
                                    "normalizedUID" to normalizedUID
                                )
                                if (userId != null) {
                                    firestore.collection("Users")
                                        .document(userId)
                                        .set(user)
                                        .addOnSuccessListener {
                                            Log.d("Registration", "User data saved successfully.")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("Registration", "Error saving user data", e)
                                        }
                                }
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
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
