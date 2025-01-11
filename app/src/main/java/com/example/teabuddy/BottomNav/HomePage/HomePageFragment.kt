package com.example.teabuddy.BottomNav.HomePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.teabuddy.R
import com.example.teabuddy.databinding.FragmentHomepageBinding
import com.google.firebase.auth.FirebaseAuth

class HomePageFragment : Fragment(R.layout.fragment_homepage) {
    private lateinit var binding: FragmentHomepageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomePageFragment", "onCreate: called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomepageBinding.inflate(inflater, container, false)

        return binding.root
    }

    }

