package com.example.teabuddy.BottomNav.ToolsPage


import android.app.Notification
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teabuddy.R
import android.widget.Toast
import com.example.teabuddy.databinding.FragmentTimerBinding
import utils.NotificationHelper


class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val NMins = binding.npMinutes
        val NSeconds = binding.npSeconds
        val startTimerBtn = binding.startBtn

        NMins.maxValue=59
        NMins.wrapSelectorWheel=true
        NSeconds.maxValue=59
        NSeconds.wrapSelectorWheel=true

        startTimerBtn.setOnClickListener(){
            val minutes = NMins.value
            val seconds = NSeconds.value

            val totalMillis = (minutes * 60 + seconds) * 1000L
            if (totalMillis > 0) {
                startTimer(totalMillis)
            } else {
                Toast.makeText(requireContext(), "Оберіть бажаний час", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun startTimer(totalMillis: Long) {
        timer?.cancel()

        timer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished % 3600000) / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.timeLeft.setText( String.format("%02d:%02d", minutes, seconds),)
            }

            override fun onFinish() {
                NotificationHelper.sendNotification(requireContext(),"Ваш чай готов!","Ваш таймер закінчив відлік! :)")
                Toast.makeText(requireContext(), "Таймер закінчився!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

}