package com.example.teabuddy.Teas
import IngredientsAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teabuddy.R
import com.example.teabuddy.databinding.ActivityTeaDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class TeaDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeaDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var ingredientsList: ArrayList<IngredientModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeaDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras
        val teaIngredients= bundle?.getStringArrayList("teaIngredients")

        binding.backBtn.setOnClickListener {
            finish()
        }
        loadText()
        loadImage()
        loadCaffeine()
        if (teaIngredients != null) {
            loadIngredients(teaIngredients.toList())
        } else {
            Log.d("TeaDetailsActivity", "No ingredients found")
        }
    }

    private fun loadText(){
        val Name: TextView = binding.teaName
        val Type: TextView = binding.teaType
        val Description: TextView = binding.textDescription
        val Time: TextView = binding.prepareTime
        val Temperature: TextView = binding.temperature

        val bundle: Bundle? = intent.extras
        val teaName = bundle?.getString("teaName")
        val teaType = bundle?.getString("teaType")

        val teaDescription = bundle?.getString("teaDescription")
        val teaTime = bundle?.getInt("teaTime")
        val teaTemperature = bundle?.getInt("teaTemperature")


        Name.text = teaName
        Type.text = teaType


        Description.text = teaDescription
        Time.text = "$teaTime${(Time.text)}"
        Temperature.text = "$teaTemperature${(Temperature.text)}"
    }
    private fun loadImage(){
        val Image: ImageView=binding.teaImage
        val teaImage = intent.getStringExtra("teaImage")
        if (!teaImage.isNullOrEmpty()) {
            Picasso.get()
                .load(teaImage)
                .placeholder(R.drawable.about_icon)
                .error(R.drawable.about_icon)
                .into(Image)
        } else {
            Log.d("TeaDetailsActivity","No photo found")
            Image.setImageResource(R.drawable.about_icon)
        }
    }
    private fun loadIngredients(ingredientIds: List<String>) {
        ingredientsList = ArrayList()
        val adapter = IngredientsAdapter(ingredientsList)
        binding.ingredientsList.layoutManager = LinearLayoutManager(this)
        binding.ingredientsList.adapter = adapter

        for (id in ingredientIds) {
            db.collection("Ingredients").document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val ingredient = document.toObject(IngredientModel::class.java)
                        if (ingredient != null) {
                            ingredientsList.add(ingredient)
                            adapter.notifyItemInserted(ingredientsList.size - 1)
                        }
                    } else {
                        Log.d("TeaDetailsActivity", "Ingredient with ID $id not found")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TeaDetailsActivity", "Failed to load ingredient with ID $id")
                }
        }
    }

    private fun loadCaffeine(){
        val CafImage = binding.caffeineIcon
        val CafText = binding.levelCaffeine
        val level = intent.getIntExtra("teaCaffeine",0)
        when (level){
            0->{
                CafImage.setBackgroundResource(R.drawable.coffee_beans)
                CafText.text = getString(R.string.NoCaffeine)
            }
            1->{
                CafImage.setBackgroundResource(R.drawable.coffee_beans)
                CafText.text = getString(R.string.LowCaffeine)
            }
            2->{
                CafImage.setBackgroundResource(R.drawable.coffee_beans_m)
                CafText.text = getString(R.string.MediumCaffeine)
            }
            3->{
                CafImage.setBackgroundResource(R.drawable.coffee_beans_h)
                CafText.text = getString(R.string.HighCaffeine)
            }
            else -> {
                CafImage.setBackgroundResource(R.drawable.coffee_beans)
            }
        }
    }
}
