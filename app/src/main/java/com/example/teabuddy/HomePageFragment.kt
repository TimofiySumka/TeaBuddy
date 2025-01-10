package com.example.teabuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.teabuddy.databinding.FragmentHomepageBinding

class HomePageFragment : Fragment(R.layout.fragment_homepage) {
    private lateinit var binding: FragmentHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomepageBinding.inflate(layoutInflater)

    }
}
