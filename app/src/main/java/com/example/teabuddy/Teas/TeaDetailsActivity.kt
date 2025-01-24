package com.example.teabuddy.Teas

import IngredientsAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teabuddy.R
import com.example.teabuddy.databinding.ActivityTeaDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class TeaDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTeaDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var ingredientsList: ArrayList<IngredientModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeaDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }


        val Name: TextView = binding.teaName
        val Type: TextView = binding.teaType
        val Image: ImageView=binding.teaImage
        val Description: TextView = binding.textDescription
        val Time: TextView = binding.prepareTime
        val Temperature: TextView = binding.temperature

        val bundle: Bundle? = intent.extras
        val teaName = bundle?.getString("teaName")
        val teaType = bundle?.getString("teaType")
        val teaImage = intent.getStringExtra("teaImage")
        val teaDescription = bundle?.getString("teaDescription")
        val teaTime = bundle?.getInt("teaTime")
        val teaTemperature = bundle?.getInt("teaTemperature")
        val teaIngredients= bundle?.getStringArrayList("teaIngredients")


        Name.text = teaName
        Type.text = teaType
        if (teaImage != null && teaImage.isNotEmpty()) {
            Picasso.get()
                .load(teaImage)
                .placeholder(R.drawable.about_icon)
                .error(R.drawable.about_icon)
                .into(Image)
        } else {
            Log.d("TeaDetailsActivity","No photo found")
            Image.setImageResource(R.drawable.about_icon)
        }

        Description.text = teaDescription
        Time.text = "$teaTime${(Time.text)}"
        Temperature.text = "$teaTemperature${(Temperature.text)}"


        if (teaIngredients != null) {
            loadIngredients(teaIngredients.toList())
        } else {
            Log.d("TeaDetailsActivity", "No ingredients found")
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
}
