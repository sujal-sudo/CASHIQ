package com.example.cashiq.UI.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.ScaleAnimation
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem
import com.example.cashiq.adapter.CustomSpinnerAdapter
import com.example.cashiq.databinding.ActivityExpenseBinding
import com.example.cashiq.model.IncomeExpense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding
    private lateinit var databaseReference: DatabaseReference
    private val userId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference for expenses
        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("expenses")

        // Set up UI elements and listeners
        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        val categories = listOf(
            CategoryItem("Food", R.drawable.baseline_food_24),
            CategoryItem("Shopping", R.drawable.shoppingcart),
            CategoryItem("Groceries", R.drawable.baseline_local_grocery_24),
            CategoryItem("Other", R.drawable.baseline_currency_bitcoin_24)
        )

        val adapter = CustomSpinnerAdapter(this, categories)
        binding.categorySpinner.adapter = adapter

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                // Handle category selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupListeners() {
        // Back button listener
        binding.backButton.setOnClickListener {
            finish() // Close the current activity
        }

        // Continue button listener
        binding.continueButton.setOnClickListener {
            animateButton(it)
            handleContinueAction()
        }
    }

    private fun animateButton(view: android.view.View) {
        val scaleAnimation = ScaleAnimation(
            1f, 0.9f, 1f, 0.9f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 100
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = ScaleAnimation.REVERSE
        view.startAnimation(scaleAnimation)
    }

    private fun handleContinueAction() {
        val amount = binding.totalAmountText.text.toString().toDoubleOrNull()
        val description = binding.description.text.toString()
        val isRecurring = binding.repeatTransaction.isChecked
        val category = (binding.categorySpinner.selectedItem as? CategoryItem)?.name ?: ""

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        // Return the amount to the DashboardActivity
        val resultIntent = Intent()
        resultIntent.putExtra("EXPENSE_AMOUNT", amount)
        setResult(RESULT_OK, resultIntent)
        finish() // Close the ExpenseActivity
    }

    private fun saveExpenseData() {
        val amount = binding.totalAmountText.text.toString().toDoubleOrNull()
        val description = binding.description.text.toString()
        val isRecurring = binding.repeatTransaction.isChecked
        val category = (binding.categorySpinner.selectedItem as? CategoryItem)?.name ?: "Expense"

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        val expenseData = IncomeExpense(amount, description, category, isRecurring)
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/expenses")

        dbRef.push().setValue(expenseData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Pass back the expense amount to DashboardActivity
                setResult(Activity.RESULT_OK, intent.putExtra("EXPENSE_AMOUNT", amount))
                Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
