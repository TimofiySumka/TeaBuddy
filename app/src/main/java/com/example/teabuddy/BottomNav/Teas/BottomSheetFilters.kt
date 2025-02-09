package com.example.teabuddy.BottomNav.Teas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Toast
import com.example.teabuddy.R
import com.example.teabuddy.databinding.FragmentBottomSheetFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFilters(private val listener: Filter.FilterListener,    private val brandList: List<BrandModel>, ) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetFiltersBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?    ): View? {
        binding = FragmentBottomSheetFiltersBinding.inflate(inflater, container, false)
        setupListeners()

        val brandNames = brandList.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brandNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.teaTypeSpinner.adapter = adapter

        return binding.root

    }
    private fun setupListeners() {
        binding.buttonClose.setOnClickListener{
            dismiss()
        }
        binding.applyButton.setOnClickListener {
            val selectedBrand = binding.teaTypeSpinner.selectedItem.toString()
            Toast.makeText(context, "Selected Brand: $selectedBrand", Toast.LENGTH_SHORT).show()
           // val selectedTeaType = binding.teaTypeSpinner.selectedItem.toString()
           // listener.onFiltersApplied(selectedTeaType)
          //  dismiss()
        }
    }

}