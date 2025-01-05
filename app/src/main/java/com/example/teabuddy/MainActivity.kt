package com.example.teabuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fs = com.google.firebase.ktx.Firebase.firestore

        val label= findViewById<TextView>(R.id.LogText)
        val userName: EditText = findViewById(R.id.LogName)
        val logButton: Button = findViewById(R.id.LogButton)

        logButton.setOnClickListener{
            var text = userName.text.toString().trim()
            if (text.isEmpty())
                Toast.makeText(this,"Заповніть поле з вашем ім'ям.",Toast.LENGTH_SHORT).show()
            else
                label.setText("Вітаємо,"+text)
        }
    }
}