package com.example.teabuddy.BottomNav.HomePage.HomePage

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.teabuddy.databinding.FragmentHomepageBinding
import com.example.teabuddy.databinding.FragmentProfileBinding

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomepageBinding.inflate(layoutInflater)

    }
}
