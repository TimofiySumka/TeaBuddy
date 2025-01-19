package com.example.teabuddy.BottomNav.Recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teabuddy.R
import com.example.teabuddy.Teas.TeaAdapter
import com.example.teabuddy.Teas.TeaModel
import com.example.teabuddy.databinding.FragmentShelfBinding
import com.google.firebase.firestore.FirebaseFirestore

class ShelfFragment : Fragment() {
    private var _binding: FragmentShelfBinding? = null
    private lateinit var teaAdapter: TeaAdapter
    private val binding get() = _binding!!

    private lateinit var teaList: ArrayList<TeaModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teaList = arrayListOf(
            TeaModel(R.drawable.tealeaf, "Sweet Tea","Red"),
            TeaModel(R.drawable.tealeaf, "Calm Tea","Pu-erh"),
            TeaModel(R.drawable.tealeaf, "Refreshing Tea","Black"),
            TeaModel(R.drawable.tealeaf, "Sweet Tea","Black"),
            TeaModel(R.drawable.tealeaf, "Calm Tea","Cei-longh"),
            TeaModel(R.drawable.tealeaf, "Refreshing Tea","Ulun")

        )
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
        teaAdapter = TeaAdapter(teaList)
        binding.teasRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.teasRv.setHasFixedSize(true)
        binding.teasRv.adapter = teaAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun importTeas(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Teas").get()
            .addOnSuccessListener { result ->
                //teaList.clear()
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
}
