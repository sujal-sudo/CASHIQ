package com.example.cashiq.UI.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.cashiq.R

class CreateBudgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_budget, container, false)

        // Initialize views
        val backArrow: ImageView = view.findViewById(R.id.back_arrow)
        val categoryDropdown: Spinner = view.findViewById(R.id.category_dropdown)
        val continueButton: Button = view.findViewById(R.id.continue_button)
        val receiveAlertSwitch: Switch = view.findViewById(R.id.receive_alert_switch)
        val slider: SeekBar = view.findViewById(R.id.slider)

        // Set up dropdown
        val categories = arrayOf("Select Category", "Food", "Transport", "Entertainment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryDropdown.adapter = adapter

        // Initially hide the slider
        slider.visibility = View.GONE

        // Dropdown item selection listener
        categoryDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) { // Ignore the first "Select Category" option
                    if (slider.visibility == View.GONE) {
                        slider.visibility = View.VISIBLE
                        // Animate the slider appearing
                        val fadeIn = AlphaAnimation(0f, 1f).apply {
                            duration = 500 // Animation duration in milliseconds
                            fillAfter = true
                        }
                        slider.startAnimation(fadeIn)
                    }
                } else {
                    // Hide the slider if no valid category is selected
                    slider.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Back arrow click listener
        backArrow.setOnClickListener {
            requireActivity().onBackPressed() // Navigate back
        }

        // Continue button click listener
        continueButton.setOnClickListener {
            val selectedCategory = categoryDropdown.selectedItem.toString()
            if (selectedCategory == "Select Category") {
                Toast.makeText(
                    requireContext(),
                    "Please select a category before continuing.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val isAlertEnabled = receiveAlertSwitch.isChecked
                val sliderValue = slider.progress

                Toast.makeText(
                    requireContext(),
                    "Category: $selectedCategory, Alert: $isAlertEnabled, Slider: $sliderValue%",
                    Toast.LENGTH_SHORT
                ).show()
                // Add logic to navigate to the next screen or save the budget
            }
        }

        return view
    }
}