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
import com.example.cashiq.model.ExpenseModel
import com.example.cashiq.viewmodel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var repeatSwitch: Switch
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var constraintLayout: ConstraintLayout

    private val expenseViewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        initializeViews()
        setupSpinner()
        setupClickListeners()
        setupCurrencyFormatting()
        observeViewModel()
    }

    private fun initializeViews() {
        amountEditText = findViewById(R.id.total_amount_text)
        categorySpinner = findViewById(R.id.category_spinner)
        descriptionEditText = findViewById(R.id.description)
        repeatSwitch = findViewById(R.id.repeat_transaction)
        continueButton = findViewById(R.id.continue_button)
        backButton = findViewById(R.id.back_button)
        constraintLayout = findViewById(R.id.myConstraintLayout)
    }

    private fun setupSpinner() {
        val categories = listOf(
            CategoryItem("Food", R.drawable.baseline_food_24),
            CategoryItem("Shopping", R.drawable.shoppingcart),
            CategoryItem("Groceries", R.drawable.baseline_local_grocery_24),
            CategoryItem("Other", R.drawable.baseline_currency_bitcoin_24)
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

        val newExpense = ExpenseModel(
            id = "",
            amount = amount.toInt(),
            category = selectedCategory.name, // Ensure category is stored
            expenseDate = formattedDate,
            expenseNote = description,
            userId = userId
        )

        expenseViewModel.addExpense(newExpense)
    }

    private fun observeViewModel() {
        expenseViewModel.operationStatus.observe(this) { (success, message) ->
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
