package com.example.cashiq.UI.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem
import com.example.cashiq.adapter.CustomSpinnerAdapter
import com.example.cashiq.databinding.ActivityIncomeBinding
import com.example.cashiq.model.IncomeExpense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class IncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomeBinding
    private val userId: String? by lazy {
        FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()

        // Handle back button
        binding.backButton.setOnClickListener { finish() }

        // Handle save income
        binding.continueButton.setOnClickListener { saveIncomeData() }
    }

    private fun setupSpinner() {
        val categories = listOf(
            CategoryItem("Salary", R.drawable.salary),
            CategoryItem("Business", R.drawable.baseline_business_24),
            CategoryItem("Investment", R.drawable.investment),
            CategoryItem("Freelance", R.drawable.freelance),
            CategoryItem("Rental", R.drawable.rental),
            CategoryItem("Others", R.drawable.others)
        )
        val adapter = CustomSpinnerAdapter(this, categories)
        binding.categorySpinner.adapter = adapter
    }

    private fun saveIncomeData() {
        val amount = binding.amountText.text.toString().toDoubleOrNull()
        val description = binding.description.text.toString()
        val isRecurring = binding.repeatTransaction.isChecked
        val category = (binding.categorySpinner.selectedItem as? CategoryItem)?.name ?: "Income"

        // Validate amount input
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate description input
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        val incomeData = IncomeExpense(amount, description, category, isRecurring)
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/income")

        dbRef.push().setValue(incomeData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Pass back the income amount to the calling activity or fragment
                setResult(Activity.RESULT_OK, intent.putExtra("INCOME_AMOUNT", amount))
                Toast.makeText(this, "Income saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save income", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
