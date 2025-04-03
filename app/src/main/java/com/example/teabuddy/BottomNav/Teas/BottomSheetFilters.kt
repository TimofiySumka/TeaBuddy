package com.example.teabuddy.BottomNav.Teas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.GridLayout
import android.widget.RelativeLayout
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
        setButtonWeights()

        val brandNames = brandList.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brandNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.teaTypeSpinner.adapter = adapter



        return binding.root

    }

    private fun setButtonWeights() {
        binding.teaTypesFlex.buttons.forEach { btn ->
            btn.applyToCards {
                it.layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

    private fun setupListeners() {
        binding.buttonClose.setOnClickListener{
            dismiss()
        }
           binding.resetButton.setOnClickListener {
           binding.teaTypesFlex.buttons.forEach { it.isSelected = false}
       }

        val selectedTeaTypes = mutableListOf<String>()


        val teaTypeMapping = mapOf(
            binding.greenTea to "green",
            binding.whiteTea to "white",
            binding.puerTea to "puer",
            binding.oolongTea to "oolong",
            binding.yellowTea to "yellow",
            binding.blackTea to "black"
        )


        teaTypeMapping.forEach { (button, teaType) ->
            button.setOnClickListener {
                if (selectedTeaTypes.contains(teaType)) {
                    selectedTeaTypes.remove(teaType)
                    button.isSelected = false
                } else {
                    selectedTeaTypes.add(teaType)
                    button.isSelected = true
                }
            }
        }

        binding.applyButton.setOnClickListener {
            val selectedBrand = binding.teaTypeSpinner.selectedItem.toString()

            Toast.makeText(context, "Бренд: $selectedBrand, Тип: $selectedTeaTypes", Toast.LENGTH_SHORT).show()


            // listener.onFiltersApplied(selectedTeaTypes, selectedBrand)

            dismiss()
        }
    }

}