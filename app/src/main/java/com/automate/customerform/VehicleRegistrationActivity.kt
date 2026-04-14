package com.automate.customerform

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class VehicleRegistrationActivity : AppCompatActivity() {

    private lateinit var mechanicNameTextView: TextView
    private lateinit var changeMechanicButton: MaterialButton
    private lateinit var vehicleRegEditText: TextInputEditText
    private lateinit var nctDateEditText: TextInputEditText
    private lateinit var mileageEditText: TextInputEditText
    private lateinit var startInspectionButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_registration)

        initializeViews()
        loadMechanicInfo()
        setupListeners()
    }

    private fun initializeViews() {
        mechanicNameTextView = findViewById(R.id.mechanicNameTextView)
        changeMechanicButton = findViewById(R.id.changeMechanicButton)
        vehicleRegEditText = findViewById(R.id.vehicleRegEditText)
        nctDateEditText = findViewById(R.id.nctDateEditText)
        mileageEditText = findViewById(R.id.mileageEditText)
        startInspectionButton = findViewById(R.id.startInspectionButton)
    }

    private fun loadMechanicInfo() {
        val prefs = getSharedPreferences("AutoMate", MODE_PRIVATE)
        val mechanicName = prefs.getString("mechanic_name", "Unknown") ?: "Unknown"
        val mechanicRole = prefs.getString("mechanic_role", "") ?: ""

        mechanicNameTextView.text = "$mechanicName${if (mechanicRole.isNotEmpty()) " ($mechanicRole)" else ""}"

        // Store mechanic name in inspection data
        InspectionData.fitterName = mechanicName
        InspectionData.mechanicName = mechanicName
    }

    private fun setupListeners() {
        changeMechanicButton.setOnClickListener {
            // Clear saved mechanic and return to selection
            getSharedPreferences("AutoMate", MODE_PRIVATE).edit().clear().apply()
            InspectionData.reset()

            val intent = Intent(this, MechanicSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Date picker for NCT date
        nctDateEditText.setOnClickListener {
            showDatePicker()
        }

        startInspectionButton.setOnClickListener {
            if (validateForm()) {
                saveVehicleDataAndProceed()
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select NCT Due Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            nctDateEditText.setText(sdf.format(Date(selection)))
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun validateForm(): Boolean {
        val vehicleReg = vehicleRegEditText.text.toString().trim()
        val mileageText = mileageEditText.text.toString().trim()

        if (vehicleReg.isEmpty()) {
            Toast.makeText(this, "Please enter vehicle registration", Toast.LENGTH_SHORT).show()
            return false
        }

        if (mileageText.isEmpty()) {
            Toast.makeText(this, "Please enter mileage", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveVehicleDataAndProceed() {
        // Save to inspection data
        InspectionData.vehicleReg = vehicleRegEditText.text.toString().trim().toUpperCase(Locale.getDefault())
        InspectionData.nctDate = nctDateEditText.text.toString().trim().ifEmpty { null }
        InspectionData.mileage = mileageEditText.text.toString().trim().toIntOrNull() ?: 0

        // Start inspection workflow
        val intent = Intent(this, InspectionStepActivity::class.java)
        intent.putExtra("step", "wipers")
        intent.putExtra("stepNumber", 1)
        intent.putExtra("totalSteps", 13)
        startActivity(intent)
    }
}
