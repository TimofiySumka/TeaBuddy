package com.example.teabuddy.BottomNav.Recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.teabuddy.R
import com.example.teabuddy.Teas.TeaAdapter
import com.example.teabuddy.Teas.TeaDetailsActivity
import com.example.teabuddy.Teas.TeaModel
import com.example.teabuddy.databinding.FragmentShelfBinding
import com.google.firebase.firestore.FirebaseFirestore

class ShelfFragment : Fragment() {
    private var _binding: FragmentShelfBinding? = null
    private val binding get() = _binding!!

    private lateinit var teaAdapter: TeaAdapter
    private lateinit var teaList: ArrayList<TeaModel>
    private lateinit var searchView :SearchView
    private lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teaList = arrayListOf()
        importTeas()
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
        teaAdapter = TeaAdapter(teaList)
        teaAdapter.setOnItemClickListener(object : TeaAdapter.onItemClickListener {


            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(),TeaDetailsActivity::class.java)
                intent.putExtra("teaName",teaList[position].name)
                intent.putExtra("teaImage",teaList[position].image)
                intent.putExtra("teaType",teaList[position].type)
                intent.putExtra("teaDescription",teaList[position].description)
                intent.putExtra("teaTime",teaList[position].time)
                intent.putExtra("teaTemperature",teaList[position].temperature)
                startActivity(intent)
            }
        })


        binding.teasRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.teasRv.setHasFixedSize(true)
        binding.teasRv.adapter = teaAdapter
        binding.button.setOnClickListener{
            addTeaToDatabase("Test Tea", "Green", R.drawable.tealeaf,"description info",12,14)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun importTeas(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Teas").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val tea = document.toObject(TeaModel::class.java)
                    teaList.add(tea)
                }
                teaAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "ShelfFragmentError:Import recyclerView documents", exception)
            }
    }

    private fun addTeaToDatabase(name: String, type: String, imageResId: Int,description:String,time:Int,temperature:Int) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Teas").get()
            .addOnSuccessListener { result ->
                val tea = hashMapOf(
                    "name" to name,
                    "type" to type,
                    "image" to imageResId,
                    "description" to description,
                    "time" to time,
                    "temperature" to temperature
                )

                db.collection("Teas")
                    .add(tea)
                    .addOnSuccessListener {
                        teaList.add(TeaModel(imageResId, name, type))
                        teaAdapter.notifyItemInserted(teaList.size - 1)
                    }

            }
    }
}
