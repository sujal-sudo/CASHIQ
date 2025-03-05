package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    private var onBudgetAdded: (() -> Unit)? = null

    companion object {
        fun newInstance(onBudgetAdded: () -> Unit): CreateBudgetFragment {
            return CreateBudgetFragment().apply {
                this.onBudgetAdded = onBudgetAdded
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_budget, container, false)

        // UI Elements
        val categoryDropdown: Spinner = view.findViewById(R.id.category_dropdown)
        val continueButton: Button = view.findViewById(R.id.continue_button)
        val slider: SeekBar = view.findViewById(R.id.slider)
        val amountEditText: EditText = view.findViewById(R.id.amount_display)

        Log.d("CreateBudgetFragment", "Fragment Created") // ✅ Debugging

        // ✅ Dropdown Setup
        val categories = arrayOf("Select Category", "Food", "Transport", "Entertainment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryDropdown.adapter = adapter

        // ✅ Sync Slider with EditText (Both ways)
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) amountEditText.setText(progress.toString()) // Update EditText
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().toIntOrNull()
                if (input != null) {
                    slider.progress = input.coerceIn(0, slider.max) // Update slider
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // ✅ Handle Budget Saving
        continueButton.setOnClickListener {
            Log.d("CreateBudgetFragment", "Continue Button Clicked") // ✅ Debugging Button Click

            val selectedCategory = categoryDropdown.selectedItem.toString()
            val budgetAmount = slider.progress

            if (selectedCategory == "Select Category" || budgetAmount == 0) {
                Toast.makeText(requireContext(), "Please select a category and set a valid budget.", Toast.LENGTH_SHORT).show()
                Log.d("CreateBudgetFragment", "Invalid Input: Category=$selectedCategory, Amount=$budgetAmount")
            } else {
                if (userId == null) {
                    Log.e("CreateBudgetFragment", "User ID is NULL!")
                    return@setOnClickListener
                }

                Log.d("CreateBudgetFragment", "Adding Budget: Category=$selectedCategory, Amount=$budgetAmount, UserID=$userId")

                budgetViewModel.addBudget(selectedCategory, budgetAmount, userId)
            }
        }

        // ✅ Observe Budget Creation Status
        budgetViewModel.operationStatus.observe(viewLifecycleOwner, Observer { (success, message) ->
            Log.d("CreateBudgetFragment", "Budget Operation: success=$success, message=$message")

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                onBudgetAdded?.invoke() // ✅ Refresh budget list
                parentFragmentManager.popBackStack() // ✅ Navigate back to budget list
            }
        })

        return view
    }
}
