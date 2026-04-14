package com.automate.customerform

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.automate.customerform.models.Mechanic
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch

class MechanicSelectionActivity : AppCompatActivity() {

    private lateinit var titleTextView: MaterialTextView
    private lateinit var mechanicSpinner: android.widget.Spinner
    private lateinit var continueButton: MaterialButton
    private lateinit var progressBar: LinearProgressIndicator

    private var mechanics: List<Mechanic> = emptyList()
    private var selectedMechanic: Mechanic? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if mechanic is already selected
        prefs = getSharedPreferences("AutoMate", MODE_PRIVATE)
        val savedMechanicName = prefs.getString("mechanic_name", null)

        if (savedMechanicName != null) {
            // Skip selection and go to main screen
            startMainActivity()
            return
        }

        setContentView(R.layout.activity_mechanic_selection)
        initializeViews()
        loadMechanics()
    }

    private fun initializeViews() {
        titleTextView = findViewById(R.id.titleTextView)
        mechanicSpinner = findViewById(R.id.mechanicSpinner)
        continueButton = findViewById(R.id.continueButton)
        progressBar = findViewById(R.id.progressBar)

        continueButton.setOnClickListener {
            if (selectedMechanic != null) {
                saveMechanicAndContinue()
            } else {
                Toast.makeText(this, "Please select a mechanic", Toast.LENGTH_SHORT).show()
            }
        }

        mechanicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) { // Skip the "Select..." placeholder
                    selectedMechanic = mechanics[position - 1]
                    continueButton.isEnabled = true
                } else {
                    selectedMechanic = null
                    continueButton.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMechanic = null
                continueButton.isEnabled = false
            }
        }
    }

    private fun loadMechanics() {
        setLoadingState(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMechanics()

                setLoadingState(false)

                if (response.isSuccessful && response.body()?.success == true) {
                    mechanics = response.body()?.mechanics ?: emptyList()
                    setupSpinner()
                } else {
                    Toast.makeText(
                        this@MechanicSelectionActivity,
                        "Failed to load mechanics",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                setLoadingState(false)
                Toast.makeText(
                    this@MechanicSelectionActivity,
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun setupSpinner() {
        val mechanicNames = mutableListOf("Select a mechanic...")
        mechanicNames.addAll(mechanics.map { "${it.name} (${it.role})" })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            mechanicNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mechanicSpinner.adapter = adapter
    }

    private fun saveMechanicAndContinue() {
        selectedMechanic?.let { mechanic ->
            // Save to SharedPreferences
            prefs.edit().apply {
                putString("mechanic_name", mechanic.name)
                putInt("mechanic_id", mechanic.id)
                putString("mechanic_role", mechanic.role)
                putString("mechanic_email", mechanic.email)
                apply()
            }

            // Go to main activity
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        // Reset inspection data for new inspection
        InspectionData.reset()

        val intent = Intent(this, VehicleRegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setLoadingState(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        mechanicSpinner.isEnabled = !isLoading
        continueButton.isEnabled = !isLoading && selectedMechanic != null
    }
}
