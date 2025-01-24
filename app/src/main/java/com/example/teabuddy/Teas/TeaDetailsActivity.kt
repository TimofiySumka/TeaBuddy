package com.example.teabuddy.Teas

import android.os.Bundle
import android.util.EventLogTags.Description
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.teabuddy.databinding.ActivityTeaDetailsBinding
import com.example.teabuddy.databinding.FragmentShelfBinding



 class TeaDetailsActivity: AppCompatActivity(){
    lateinit var binding: ActivityTeaDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeaDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{

            finish()
        }

        val Name : TextView = binding.teaName
        val Type : TextView = binding.teaType
        val Image : ImageView = binding.teaImage
        val Description:TextView = binding.textDescription
        val Time:TextView=binding.prepareTime
        val Temperature:TextView=binding.temperature

        val bundle: Bundle? = intent.extras
        val teaName= bundle?.getString("teaName")
        val teaType= bundle?.getString("teaType")
        val teaImage= bundle!!.getInt("teaImage")
        val teaDescription= bundle.getString("teaDescription")
        val teaTime=bundle.getInt("teaTime")
        val teaTemperature=bundle.getInt("teaTemperature")

        Name.text= teaName
        Type.text=teaType
        Image.setImageResource(teaImage)
        Description.text=teaDescription
        Time.text="$teaTime${(Time.text)}"
        Temperature.text="$teaTemperature${(Temperature.text)}"
    }

}
