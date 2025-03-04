package com.example.cashiq.UI.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem
import com.example.cashiq.adapter.CustomSpinnerAdapter
import com.example.cashiq.model.IncomeModel
import com.example.cashiq.viewmodel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IncomeActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var repeatSwitch: Switch
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var constraintLayout: ConstraintLayout

    private val incomeViewModel: IncomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        initializeViews()
        setupSpinner()
        setupClickListeners()
        setupCurrencyFormatting()
        observeViewModel()
    }

    private fun initializeViews() {
        categorySpinner = findViewById(R.id.category_spinner)
        descriptionEditText = findViewById(R.id.description)
        repeatSwitch = findViewById(R.id.repeat_transaction)
        continueButton = findViewById(R.id.continue_button)
        backButton = findViewById(R.id.backButton)
        amountEditText = findViewById(R.id.amountText)
        constraintLayout = findViewById(R.id.myConstraintLayout)
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
        categorySpinner.adapter = adapter
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        continueButton.setOnClickListener {
            animateButton(it)
            handleContinueAction()
        }

        constraintLayout.setOnTouchListener { view, _ ->
            hideKeyboard(view)
            true
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun setupCurrencyFormatting() {
        amountEditText.addTextChangedListener(object : TextWatcher {
            private var currentText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != currentText) {
                    amountEditText.removeTextChangedListener(this)
                    val cleanString = s.toString().replace("[,]".toRegex(), "")
                    if (cleanString.isNotEmpty()) {
                        val formatted = formatToNepaliCurrency(cleanString.toLong())
                        currentText = formatted
                        amountEditText.setText(formatted)
                        amountEditText.setSelection(formatted.length)
                    }
                    amountEditText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun handleContinueAction() {
        val amount = amountEditText.text.toString().replace(",", "").toDoubleOrNull()
        val description = descriptionEditText.text.toString()
        val selectedCategory = categorySpinner.selectedItem as? CategoryItem

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCategory == null) {
            Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val formattedDate = getFormattedDate()

        val newIncome = IncomeModel(
            id = "",
            amount = amount.toInt(),
            category = selectedCategory.name, // Ensure category is stored
            incomeDate = formattedDate,
            incomeNote = description,
            userId = userId
        )

        incomeViewModel.addIncome(newIncome)
    }

    private fun observeViewModel() {
        incomeViewModel.operationStatus.observe(this) { status ->
            val (success, message) = status
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (success) finish()
        }
    }


    private fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun formatToNepaliCurrency(value: Long): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN"))
        val decimalFormat = DecimalFormat("##,##,###", symbols)
        return decimalFormat.format(value)
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
