package com.example.cashiq.UI.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
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


        // Using OnBackPressedDispatcher to handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // If there's a currently focused view (keyboard is visible), hide the keyboard
                val currentFocusView = currentFocus
                if (currentFocusView != null) {
                    hideKeyboard(currentFocusView)
                } else {
                    // If no keyboard is visible, allow the default back press action (app closing)
                    isEnabled = false  // Disable this callback temporarily
                    onBackPressedDispatcher.onBackPressed()  // Use onBackPressedDispatcher to call back press behavior
                }
            }
        })

    }

    private fun initializeViews(){
        // Initialize views
        constraintLayout = findViewById(R.id.myConstraintLayout)
    }

    private fun setOnClickListeners(){

        constraintLayout.setOnTouchListener { view, event ->
            // Hide the keyboard when touched anywhere on ConstraintLayout
            hideKeyboard(view)
            true  // Return true to indicate that the touch event was consumed
        }
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus

        // If there's a currently focused view, hide the keyboard using its window token
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }



}