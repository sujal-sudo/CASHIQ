package com.example.cashiq.UI.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem
import com.example.cashiq.adapter.CustomSpinnerAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class IncomeActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var repeatSwitch: Switch
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var repeatText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        initializeViews()
        setupSpinner()
        setupClickListeners()
        setupCurrencyFormatting()
    }

    private fun initializeViews() {
        categorySpinner = findViewById(R.id.category_spinner)
        descriptionEditText = findViewById(R.id.description)
        repeatSwitch = findViewById(R.id.repeat_transaction)
        continueButton = findViewById(R.id.continue_button)
        backButton = findViewById(R.id.backButton)
        amountEditText = findViewById(R.id.amountText)
        repeatText = findViewById(R.id.repeat_text)
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

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {}

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        continueButton.setOnClickListener {
            animateButton(it)
            handleContinueAction()
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
        val isRecurring = repeatSwitch.isChecked
        val category = (categorySpinner.selectedItem as? CategoryItem)?.name ?: ""

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent()
        resultIntent.putExtra("INCOME_AMOUNT", amount)
        setResult(RESULT_OK, resultIntent)
        finish()
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
