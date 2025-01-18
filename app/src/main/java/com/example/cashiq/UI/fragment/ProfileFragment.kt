package com.example.cashiq.UI.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cashiq.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var loggedInUsername: String = "CurrentUsername"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the initial username
        binding.tvUsername.text = loggedInUsername

        // Set up click listeners
        setupClickListeners()

        // Edit username click listener
        binding.ivEditUsername.setOnClickListener {
            showEditUsernameDialog()
        }
    }

    private fun showEditUsernameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Username")

        // Set up the input
        val input = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(binding.tvUsername.text)
        }
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            val newUsername = input.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                loggedInUsername = newUsername // Update the stored username
                binding.tvUsername.text = newUsername // Update the TextView
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun setupClickListeners() {
        binding.apply {
            // Account section click listener
            layoutAccount.setOnClickListener {
                // Navigate to account section
            }

            // Settings section click listener
            layoutSettings.setOnClickListener {
                // Navigate to settings section
            }

            // Export data click listener
            layoutExportData.setOnClickListener {
                // Handle data export
            }

            // Logout section click listener
            layoutLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Perform logout
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}