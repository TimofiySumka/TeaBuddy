package com.example.teabuddy.BottomNav.ToolsPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teabuddy.R
import com.example.teabuddy.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {
    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        setupViewPagerAndTabs()
        return binding.root
    }

    private fun setupViewPagerAndTabs() {
        // Настройка адаптера для ViewPager
        val toolsAdapter = ToolsAdapter(childFragmentManager)
        toolsAdapter.addFragment(TimerFragment(), "Таймер")
        toolsAdapter.addFragment(BreewingFragment(), "Готування")


        binding.viewPagerTools.adapter = toolsAdapter

        // Связь ViewPager с TabLayout
        binding.tableLayout.setupWithViewPager(binding.viewPagerTools)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
