package com.example.teabuddy.BottomNav.Teas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teabuddy.Teas.TeaAdapter
import com.example.teabuddy.Teas.TeaDetailsActivity
import com.example.teabuddy.Teas.TeaModel
import com.example.teabuddy.databinding.FragmentShelfBinding
import com.google.firebase.firestore.FirebaseFirestore

class ShelfFragment : Fragment() {
    private var _binding: FragmentShelfBinding? = null
    private val binding get() = _binding!!
    private  var success: Boolean = true

    private lateinit var teaAdapter: TeaAdapter
    private lateinit var teaList: ArrayList<TeaModel>
    private lateinit var brandList: ArrayList<BrandModel>
    private lateinit var searchView :SearchView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandList= arrayListOf()
        teaList = arrayListOf()
        importTeas()
        importBrands()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShelfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = binding.searchview
        searchView.clearFocus()
        teaAdapter = TeaAdapter(teaList)

        binding.filtersButton.setOnClickListener {
            val bottomSheet = BottomSheetFilters(object : Filter.FilterListener {
                override fun onFilterComplete(p0: Int) {}
            },brandList)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.clearFocus()
                val success = teaAdapter.filter(newText)
                if (!success) {
                    binding.NotFoundLayout.visibility = View.VISIBLE
                } else {
                    binding.NotFoundLayout.visibility = View.INVISIBLE
                }
                return true
            }

        })

        teaAdapter.setOnItemClickListener(object : TeaAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(),TeaDetailsActivity::class.java)
                val ingredients = teaList[position].ingredients

                intent.putExtra("teaName",teaList[position].name)
                intent.putExtra("teaImage",teaList[position].image)
                intent.putExtra("teaType",teaList[position].type)
                intent.putExtra("teaDescription",teaList[position].description)
                intent.putExtra("teaTime",teaList[position].time)
                intent.putExtra("teaTemperature",teaList[position].temperature)
                intent.putExtra("teaCaffeine",teaList[position].caffeine)
                intent.putExtra("teaIngredients", ArrayList(ingredients))
                startActivity(intent)
            }
        })


        binding.teasRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.teasRv.setHasFixedSize(true)
        binding.teasRv.adapter = teaAdapter
        binding.button.setOnClickListener {
            val ingredientIds = listOf("123")
            addTeaToDatabase(
                "Test Tea",
                "Green",
                "https://firebasestorage.googleapis.com/v0/b/teabuddy-e6bea.firebasestorage.app/o/TeaImages%2Flovare_strawberry_champagne.jpg?alt=media&token=100b4e02-fac3-40a2-9ac1-7fe1b64bef82",
                "description info",
                12,
                14,
                0,
                ingredientIds)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun importBrands(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Brands").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val brand = document.toObject(BrandModel::class.java)
                    brandList.add(brand)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "ShelfFragmentError:Import Brands error", exception)
            }
    }

    private fun importTeas(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Teas").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val tea = document.toObject(TeaModel::class.java)
                    teaList.add(tea)
                }
                teaAdapter.updateFullTeaList(teaList)
                teaAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "ShelfFragmentError:Import recyclerView documents", exception)
            }
    }


    private fun addTeaToDatabase(name: String, type: String, imageResId: String, description: String, time: Int, temperature: Int, caffeine:Int,ingredientIds: List<String>) {
        val db = FirebaseFirestore.getInstance()

        val tea = hashMapOf(
            "name" to name,
            "type" to type,
            "image" to imageResId,
            "description" to description,
            "time" to time,
            "temperature" to temperature,
            "caffeine" to caffeine,
            "ingredients" to ingredientIds
        )

        db.collection("Teas")
            .add(tea)
            .addOnSuccessListener {
                teaList.add(TeaModel(imageResId, name, type))
                teaAdapter.notifyItemInserted(teaList.size - 1)
            }
    }
}
