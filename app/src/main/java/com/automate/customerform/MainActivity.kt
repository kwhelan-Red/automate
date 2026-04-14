package com.automate.customerform

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mechanicNameTextView: TextView
    private lateinit var changeMechanicButton: MaterialButton

    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var phoneInputLayout: TextInputLayout
    private lateinit var companyInputLayout: TextInputLayout
    private lateinit var notesInputLayout: TextInputLayout

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var companyEditText: TextInputEditText
    private lateinit var notesEditText: TextInputEditText

    private lateinit var submitButton: MaterialButton
    private lateinit var progressBar: android.widget.ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        loadMechanicInfo()
        setupListeners()
    }

    private fun initializeViews() {
        mechanicNameTextView = findViewById(R.id.mechanicNameTextView)
        changeMechanicButton = findViewById(R.id.changeMechanicButton)
        nameInputLayout = findViewById(R.id.nameInputLayout)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        phoneInputLayout = findViewById(R.id.phoneInputLayout)
        companyInputLayout = findViewById(R.id.companyInputLayout)
        notesInputLayout = findViewById(R.id.notesInputLayout)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        companyEditText = findViewById(R.id.companyEditText)
        notesEditText = findViewById(R.id.notesEditText)

        submitButton = findViewById(R.id.submitButton)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loadMechanicInfo() {
        val prefs = getSharedPreferences("AutoMate", MODE_PRIVATE)
        val mechanicName = prefs.getString("mechanic_name", "Unknown") ?: "Unknown"
        val mechanicRole = prefs.getString("mechanic_role", "") ?: ""

        mechanicNameTextView.text = "$mechanicName${if (mechanicRole.isNotEmpty()) " ($mechanicRole)" else ""}"
    }

    private fun setupListeners() {
        changeMechanicButton.setOnClickListener {
            // Clear saved mechanic and return to selection
            getSharedPreferences("AutoMate", MODE_PRIVATE).edit().clear().apply()

            val intent = Intent(this, MechanicSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }

        submitButton.setOnClickListener {
            if (validateForm()) {
                submitCustomerData()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Clear previous errors
        nameInputLayout.error = null
        emailInputLayout.error = null
        phoneInputLayout.error = null
        companyInputLayout.error = null

        // Validate name
        val name = nameEditText.text.toString().trim()
        if (name.isEmpty()) {
            nameInputLayout.error = "Name is required"
            isValid = false
        }

        // Validate email
        val email = emailEditText.text.toString().trim()
        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = getString(R.string.invalid_email)
            isValid = false
        }

        // Validate phone
        val phone = phoneEditText.text.toString().trim()
        if (phone.isEmpty()) {
            phoneInputLayout.error = "Phone is required"
            isValid = false
        }

        // Company is optional but we'll include it
        // Notes are optional

        return isValid
    }

    private fun submitCustomerData() {
        val customerData = CustomerData(
            name = nameEditText.text.toString().trim(),
            email = emailEditText.text.toString().trim(),
            phone = phoneEditText.text.toString().trim(),
            company = companyEditText.text.toString().trim(),
            notes = notesEditText.text.toString().trim()
        )

        // Show loading state
        setLoadingState(true)

        // Make API call using coroutines
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.submitCustomer(customerData)

                setLoadingState(false)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.success_message),
                        Toast.LENGTH_LONG
                    ).show()
                    clearForm()
                } else {
                    val errorMessage = response.body()?.message ?: getString(R.string.error_message)
                    Toast.makeText(
                        this@MainActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                setLoadingState(false)
                Toast.makeText(
                    this@MainActivity,
                    "${getString(R.string.error_message)} ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        submitButton.isEnabled = !isLoading
        submitButton.text = if (isLoading) getString(R.string.submitting) else getString(R.string.submit_button)
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        // Disable input fields while loading
        nameEditText.isEnabled = !isLoading
        emailEditText.isEnabled = !isLoading
        phoneEditText.isEnabled = !isLoading
        companyEditText.isEnabled = !isLoading
        notesEditText.isEnabled = !isLoading
    }

    private fun clearForm() {
        nameEditText.text?.clear()
        emailEditText.text?.clear()
        phoneEditText.text?.clear()
        companyEditText.text?.clear()
        notesEditText.text?.clear()

        // Clear focus
        nameEditText.clearFocus()
    }
}
