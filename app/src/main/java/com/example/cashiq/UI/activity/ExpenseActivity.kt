package com.example.cashiq.UI.activity

import com.example.cashiq.adapter.CustomSpinnerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem

class ExpenseActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var repeatSwitch: Switch
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var totalAmountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense) // Update with your actual layout name

        // Initialize views
        categorySpinner = findViewById(R.id.category_spinner)
        descriptionEditText = findViewById(R.id.description)
        repeatSwitch = findViewById(R.id.repeat_transaction)
        continueButton = findViewById(R.id.continue_button)
        backButton = findViewById(R.id.back_button)
        totalAmountTextView = findViewById(R.id.total_amount_text) // Make sure this view exists in your layout

        // Set up the Spinner with the com.example.cashiq.adapter.CustomSpinnerAdapter
        val categories = listOf(
            CategoryItem("Food", R.drawable.baseline_food_24),
            CategoryItem("Shopping", R.drawable.shoppingcart),
            CategoryItem("Groceries", R.drawable.baseline_local_grocery_24),
            CategoryItem("Other", R.drawable.baseline_currency_bitcoin_24)
        )

        val adapter = CustomSpinnerAdapter(this, categories)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Handle category selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up the back button
        backButton.setOnClickListener {
            // Create an intent to navigate to DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent) // Start DashboardActivity
            finish() // Optionally finish the current activity if you don't want to return to it
        }

        // Set up the button click listener
        continueButton.setOnClickListener {
            animateButton(it)
            calculateTotalAmount()
            // Handle the continue action here (e.g., save data, navigate)
        }
    }

    private fun animateButton(view: View) {
        val scaleAnimation = ScaleAnimation(
            1f, 0.9f, 1f, 0.9f, // Start and end scale
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot X
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f // Pivot Y
        )
        scaleAnimation.duration = 100 // Animation duration in milliseconds
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = ScaleAnimation.REVERSE
        view.startAnimation(scaleAnimation)
    }

    private fun calculateTotalAmount() {
        val amountStr = amountEditText.text.toString()
        val amount = amountStr.toDoubleOrNull() ?: 0.0
        totalAmountTextView.text = "Total Amount: $amount"
    }
}