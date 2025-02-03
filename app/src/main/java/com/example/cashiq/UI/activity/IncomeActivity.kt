package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem
import com.example.cashiq.adapter.CustomSpinnerAdapter
import com.example.cashiq.databinding.ActivityIncomeBinding
import com.example.cashiq.model.IncomeExpense
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.List
import java.util.Locale

class IncomeActivity : AppCompatActivity() {
    private var binding: ActivityIncomeBinding? = null
    private val userId = if (FirebaseAuth.getInstance().currentUser != null)
        FirebaseAuth.getInstance().currentUser!!.uid
    else
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initializeViews()
        setupSpinner()
        setupCurrencyFormatting()
        setupClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentFocus != null) {
                    hideKeyboard(currentFocus)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initializeViews() {
        // View binding is already initialized in onCreate
    }

    private fun setupSpinner() {
        val categories = List.of(
            CategoryItem("Salary", R.drawable.salary),
            CategoryItem("Business", R.drawable.baseline_business_24),
            CategoryItem("Investment", R.drawable.investment),
            CategoryItem("Freelance", R.drawable.freelance),
            CategoryItem("Rental", R.drawable.rental),
            CategoryItem("Others", R.drawable.others)
        )
        val adapter = CustomSpinnerAdapter(this, categories)
        binding!!.categorySpinner.adapter = adapter

        binding!!.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupCurrencyFormatting() {
        binding!!.amountText.addTextChangedListener(object : TextWatcher {
            private var currentText = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != currentText) {
                    binding!!.amountText.removeTextChangedListener(this)
                    val cleanString = s.toString().replace(",", "")
                    if (!cleanString.isEmpty()) {
                        val formatted = formatToNepaliCurrency(cleanString.toLong())
                        currentText = formatted
                        binding!!.amountText.setText(formatted)
                        binding!!.amountText.setSelection(formatted.length)
                    }
                    binding!!.amountText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding!!.backButton.setOnClickListener { v -> finish() }

        binding!!.continueButton.setOnClickListener { v ->
            animateButton(v)
            saveIncomeData()
        }

        binding!!.myConstraintLayout.setOnTouchListener { v, event ->
            hideKeyboard(v)
            true
        }
    }

    private fun hideKeyboard(view: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun formatToNepaliCurrency(value: Long): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN"))
        val decimalFormat = DecimalFormat("##,##,###", symbols)
        return decimalFormat.format(value)
    }

    private fun saveIncomeData() {
        val amountText = binding!!.amountText.text.toString().replace(",", "")
        val amount = if (amountText.isEmpty()) null else amountText.toDouble()
        val description = binding!!.description.text.toString()
        val isRecurring = binding!!.repeatTransaction.isChecked
        val category = (binding!!.categorySpinner.selectedItem as CategoryItem).name

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        val incomeData = IncomeExpense(amount, description, category, isRecurring)
        FirebaseDatabase.getInstance().getReference("users/$userId/income")
            .push().setValue(incomeData)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("INCOME_AMOUNT", amount)
                    setResult(RESULT_OK, resultIntent)
                    Toast.makeText(this, "Income saved successfully!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save income", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun animateButton(view: View) {
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
}