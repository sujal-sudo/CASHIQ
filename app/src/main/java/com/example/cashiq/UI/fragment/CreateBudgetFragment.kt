package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cashiq.R
import com.example.cashiq.viewmodel.BudgetViewModel
import com.google.firebase.auth.FirebaseAuth

class CreateBudgetFragment : Fragment() {

    private val budgetViewModel: BudgetViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var onBudgetAdded: (() -> Unit)? = null  // Callback for updating the list

    companion object {
        fun newInstance(onBudgetAdded: () -> Unit): CreateBudgetFragment {
            return CreateBudgetFragment().apply {
                this.onBudgetAdded = onBudgetAdded  // Assign callback
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_budget, container, false)

        // Initialize UI elements
        val categoryDropdown: Spinner = view.findViewById(R.id.category_dropdown)
        val continueButton: Button = view.findViewById(R.id.continue_button)
        val slider: SeekBar = view.findViewById(R.id.slider)
        val sliderValueText: TextView = view.findViewById(R.id.slider_value)

        // Dropdown Setup
        val categories = arrayOf("Select Category", "Food", "Transport", "Entertainment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryDropdown.adapter = adapter

        // Update slider text dynamically
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValueText.text = "Budget: NPR $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Continue Button Click
        continueButton.setOnClickListener {
            val selectedCategory = categoryDropdown.selectedItem.toString()
            val budgetAmount = slider.progress

            if (selectedCategory == "Select Category" || budgetAmount == 0) {
                Toast.makeText(requireContext(), "Please select a category and set a valid budget.", Toast.LENGTH_SHORT).show()
            } else {
                userId?.let { uid ->
                    budgetViewModel.addBudget(selectedCategory, budgetAmount, uid)
                }
            }
        }

        // Observe the status of the budget creation
        budgetViewModel.operationStatus.observe(viewLifecycleOwner, Observer { (success, message) ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                onBudgetAdded?.invoke()  // ✅ Call the callback after adding a budget
                parentFragmentManager.popBackStack() // Go back after saving
            }
        })

        return view
    }
}
