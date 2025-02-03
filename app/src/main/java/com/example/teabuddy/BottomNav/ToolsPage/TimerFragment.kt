package com.example.teabuddy.BottomNav.ToolsPage


import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teabuddy.R
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.teabuddy.TimerService
import com.example.teabuddy.databinding.FragmentTimerBinding
import utils.NotificationHelper
import java.util.Timer


class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var brewingTime: Int = 0

    private var isPaused = false
    private var savedTime: Long = 0L


    // BroadcastReceiver для оновлення  часу
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val remainingTime = intent.getLongExtra("remainingTime", 0L)

            if (remainingTime > 0) {
                val minutes = (remainingTime / 1000) / 60
                val seconds = (remainingTime / 1000) % 60
                binding.timeLeft.text = String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        arguments?.let {
            brewingTime = it.getInt("BREWING_TIME", 0)
        }

        if (brewingTime > 0) {
            val minutes = brewingTime / 60
            binding.npMinutes.value = minutes
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Реєстрація страция BroadcastReceiver
        val filter = IntentFilter("com.example.teabuddy.TIMER_UPDATE")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter)

        val NMins = binding.npMinutes
        val NSeconds = binding.npSeconds
        val startTimerBtn = binding.startBtn

        NMins.maxValue = 59
        NMins.wrapSelectorWheel = true
        NSeconds.maxValue = 59
        NSeconds.wrapSelectorWheel = true

        startTimerBtn.setOnClickListener {
            if(!TimerService.working){
                startTimerBtn.setImageResource(R.drawable.icon_pause)
                val minutes = NMins.value
                val seconds = NSeconds.value

                val totalMillis = (minutes * 60 + seconds) * 1000L
                if (totalMillis > 0) {
                    startTimer(totalMillis)
                } else {
                    Toast.makeText(requireContext(), "Оберіть бажаний час", Toast.LENGTH_SHORT).show()
                }
                TimerService.working=true
            }
            else{
                startTimerBtn.setImageResource(R.drawable.ic_play)

                TimerService.working=false
            }
        }
    }

    private fun startTimer(totalMillis: Long) {
        val serviceIntent = Intent(requireContext(), TimerService::class.java).apply {
            putExtra(TimerService.EXTRA_TIME, totalMillis)
        }
        requireContext().startService(serviceIntent)
    }

    private fun stopTimer() {
        val serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireContext().stopService(serviceIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}
