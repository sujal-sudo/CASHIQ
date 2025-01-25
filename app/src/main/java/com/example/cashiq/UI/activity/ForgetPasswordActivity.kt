package com.example.cashiq.UI.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cashiq.R

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)


        initializeViews()
        setOnClickListeners()

    }

    private fun initializeViews(){
        // Initialize views
        constraintLayout = findViewById(R.id.myConstraintLayout)
    }

    private fun setOnClickListeners(){

        constraintLayout.setOnTouchListener { _, _ ->
            hideKeyboardAndClearFocus()
            true // Consume the touch event
        }
    }


    private fun hideKeyboardAndClearFocus() {
        // Hide the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        // Clear focus from the currently focused view
        currentFocus?.clearFocus()

        //  set focus to the layout
        constraintLayout.requestFocus()
    }



}