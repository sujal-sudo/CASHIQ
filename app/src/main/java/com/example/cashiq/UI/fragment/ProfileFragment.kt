package com.example.cashiq.UI.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cashiq.R
import com.example.cashiq.UI.activity.LoginActivity
import com.example.cashiq.databinding.FragmentProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var loggedInUsername: String = "User"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = auth.currentUser
    private val database = FirebaseDatabase.getInstance()

    // Reference to the users collection in the database
    private val usersRef = database.getReference("users")

    // Profile color array
    private val PROFILE_COLORS = arrayOf(
        "#F44336", // Red
        "#E91E63", // Pink
        "#9C27B0", // Purple
        "#673AB7", // Deep Purple
        "#3F51B5", // Indigo
        "#2196F3", // Blue
        "#03A9F4", // Light Blue
        "#00BCD4", // Cyan
        "#009688", // Teal
        "#4CAF50", // Green
        "#8BC34A", // Light Green
        "#FF9800", // Orange
        "#FF5722"  // Deep Orange
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show loading state while fetching data
        showLoadingState(true)

        // Fetch username from Firebase Database
        fetchUsernameFromDatabase()

        // Click Listeners
        setupClickListeners()

        // Edit username click listener
        binding.ivEditUsername.setOnClickListener {
            showEditUsernameDialog()
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.profileLoading.visibility = View.VISIBLE
            binding.profileContent.visibility = View.GONE
        } else {
            binding.profileLoading.visibility = View.GONE
            binding.profileContent.visibility = View.VISIBLE
        }
    }

    private fun fetchUsernameFromDatabase() {
        user?.uid?.let { userId ->
            usersRef.child(userId).child("username").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.getValue(String::class.java)

                    if (username != null) {
                        // Update username if found in database
                        loggedInUsername = username
                        binding.tvUsername.text = username

                        // Set up profile icon with the username
                        setupProfileIcon()
                    } else {
                        // If no username in database, use displayName from Auth
                        loggedInUsername = user?.displayName ?: "User"
                        binding.tvUsername.text = loggedInUsername

                        // Save this username to database for first-time users
                        if (loggedInUsername != "User") {
                            saveUsernameToDatabase(loggedInUsername)
                        }

                        // Set up profile icon with the username
                        setupProfileIcon()
                    }

                    // Hide loading state
                    showLoadingState(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    // On error, fall back to Auth displayName
                    loggedInUsername = user?.displayName ?: "User"
                    binding.tvUsername.text = loggedInUsername
                    setupProfileIcon()

                    // Hide loading state
                    showLoadingState(false)

                    Toast.makeText(context, "Failed to load profile data", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: run {
            // If user is null, use default
            loggedInUsername = "User"
            binding.tvUsername.text = loggedInUsername
            setupProfileIcon()

            // Hide loading state
            showLoadingState(false)
        }
    }

    private fun setupProfileIcon() {
        // Generate color based on username
        val colorIndex = Math.abs(loggedInUsername.hashCode()) % PROFILE_COLORS.size
        val profileColor = Color.parseColor(PROFILE_COLORS[colorIndex])

        // Set the background color for the profile
        binding.profileBackground.setBackgroundColor(profileColor)
    }

    private fun showEditUsernameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Username")

        val input = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(binding.tvUsername.text)
        }
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newUsername = input.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                updateUsername(newUsername)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun updateUsername(newUsername: String) {
        // Show loading while updating
        binding.tvUsername.alpha = 0.5f

        // First update in Firebase Auth
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(newUsername)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                // Then update in Firebase Database
                saveUsernameToDatabase(newUsername)
            } else {
                binding.tvUsername.alpha = 1.0f
                Toast.makeText(requireContext(), "Failed to update username.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUsernameToDatabase(username: String) {
        user?.uid?.let { userId ->
            usersRef.child(userId).child("username").setValue(username)
                .addOnSuccessListener {
                    // Update UI and local data on success
                    loggedInUsername = username
                    binding.tvUsername.text = username
                    binding.tvUsername.alpha = 1.0f

                    // Update profile icon color when username changes
                    setupProfileIcon()

                    Toast.makeText(requireContext(), "Username updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    binding.tvUsername.alpha = 1.0f
                    Toast.makeText(requireContext(), "Failed to save username to database.", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            binding.tvUsername.alpha = 1.0f
            Toast.makeText(requireContext(), "User not authenticated.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            // Account settings (Change Password)
            layoutAccount.setOnClickListener {
                showChangePasswordDialog()
            }

            // Settings (placeholder for now)
            layoutSettings.setOnClickListener {
                Toast.makeText(requireContext(), "Settings coming soon!", Toast.LENGTH_SHORT).show()
            }

            // Logout confirmation
            layoutLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showChangePasswordDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Change Password")

        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)

        val currentPassword = layout.findViewById<EditText>(R.id.etCurrentPassword)
        val newPassword = layout.findViewById<EditText>(R.id.etNewPassword)

        builder.setView(layout)

        builder.setPositiveButton("Update") { dialog, _ ->
            val currentPass = currentPassword.text.toString().trim()
            val newPass = newPassword.text.toString().trim()

            if (currentPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show()
            } else {
                updatePassword(currentPass, newPass)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updatePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            // Reauthenticate the user before changing the password
            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to update password.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Current password is incorrect.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()

                auth.signOut() // Logout from Firebase

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}