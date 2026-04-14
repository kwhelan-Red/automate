package com.automate.customerform

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class InspectionStepActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var statusRadioGroup: RadioGroup
    private lateinit var passRadio: RadioButton
    private lateinit var advisoryRadio: RadioButton
    private lateinit var failRadio: RadioButton
    private lateinit var notesEditText: TextInputEditText
    private lateinit var nextButton: MaterialButton
    private lateinit var backButton: MaterialButton

    private var currentStep: String = ""
    private var stepNumber: Int = 1
    private var totalSteps: Int = 13

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_step)

        currentStep = intent.getStringExtra("step") ?: "wipers"
        stepNumber = intent.getIntExtra("stepNumber", 1)
        totalSteps = intent.getIntExtra("totalSteps", 13)

        initializeViews()
        setupStep()
        loadExistingData()
        setupListeners()
    }

    private fun initializeViews() {
        titleTextView = findViewById(R.id.titleTextView)
        progressTextView = findViewById(R.id.progressTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        statusRadioGroup = findViewById(R.id.statusRadioGroup)
        passRadio = findViewById(R.id.passRadio)
        advisoryRadio = findViewById(R.id.advisoryRadio)
        failRadio = findViewById(R.id.failRadio)
        notesEditText = findViewById(R.id.notesEditText)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupStep() {
        progressTextView.text = "Step $stepNumber of $totalSteps"

        when (currentStep) {
            "wipers" -> {
                titleTextView.text = "Wipers"
                descriptionTextView.text = "Check windscreen wipers for proper operation and blade condition"
            }
            "horn" -> {
                titleTextView.text = "Horn"
                descriptionTextView.text = "Test horn for proper operation"
            }
            "front_lights" -> {
                titleTextView.text = "Front Lights"
                descriptionTextView.text = "Check all front lights (headlights, indicators, fog lights)"
            }
            "rear_lights" -> {
                titleTextView.text = "Rear Lights"
                descriptionTextView.text = "Check all rear lights (tail lights, brake lights, indicators, reverse lights)"
            }
            "side_lights" -> {
                titleTextView.text = "Side Lights"
                descriptionTextView.text = "Check side marker lights and reflectors"
            }
            "spare_wheel" -> {
                titleTextView.text = "Spare Wheel"
                descriptionTextView.text = "Check spare wheel condition and pressure"
            }
            "front_pads" -> {
                titleTextView.text = "Front Brake Pads"
                descriptionTextView.text = "Check front brake pad thickness and condition"
            }
            "rear_pads" -> {
                titleTextView.text = "Rear Brake Pads"
                descriptionTextView.text = "Check rear brake pad thickness and condition"
            }
            "front_disks" -> {
                titleTextView.text = "Front Brake Disks"
                descriptionTextView.text = "Check front brake disk condition and wear"
            }
            "rear_disks" -> {
                titleTextView.text = "Rear Brake Disks"
                descriptionTextView.text = "Check rear brake disk condition and wear"
            }
            "alignment" -> {
                titleTextView.text = "Wheel Alignment"
                descriptionTextView.text = "Check wheel alignment and steering"
            }
        }
    }

    private fun loadExistingData() {
        val (status, notes) = when (currentStep) {
            "wipers" -> Pair(InspectionData.wipers, InspectionData.wipersNotes)
            "horn" -> Pair(InspectionData.horn, InspectionData.hornNotes)
            "front_lights" -> Pair(InspectionData.frontLights, InspectionData.frontLightsNotes)
            "rear_lights" -> Pair(InspectionData.rearLights, InspectionData.rearLightsNotes)
            "side_lights" -> Pair(InspectionData.sideLights, InspectionData.sideLightsNotes)
            "spare_wheel" -> Pair(InspectionData.spareWheel, InspectionData.spareWheelNotes)
            "front_pads" -> Pair(InspectionData.frontPads, InspectionData.frontPadsNotes)
            "rear_pads" -> Pair(InspectionData.rearPads, InspectionData.rearPadsNotes)
            "front_disks" -> Pair(InspectionData.frontDisks, InspectionData.frontDisksNotes)
            "rear_disks" -> Pair(InspectionData.rearDisks, InspectionData.rearDisksNotes)
            "alignment" -> Pair(InspectionData.alignment, InspectionData.alignmentNotes)
            else -> Pair("pass", "")
        }

        when (status) {
            "pass" -> passRadio.isChecked = true
            "advisory" -> advisoryRadio.isChecked = true
            "fail" -> failRadio.isChecked = true
        }

        notesEditText.setText(notes)
    }

    private fun setupListeners() {
        nextButton.setOnClickListener {
            saveDataAndProceed()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveDataAndProceed() {
        val status = when (statusRadioGroup.checkedRadioButtonId) {
            R.id.passRadio -> "pass"
            R.id.advisoryRadio -> "advisory"
            R.id.failRadio -> "fail"
            else -> "pass"
        }

        val notes = notesEditText.text.toString().trim()

        // Save to InspectionData
        when (currentStep) {
            "wipers" -> {
                InspectionData.wipers = status
                InspectionData.wipersNotes = notes
            }
            "horn" -> {
                InspectionData.horn = status
                InspectionData.hornNotes = notes
            }
            "front_lights" -> {
                InspectionData.frontLights = status
                InspectionData.frontLightsNotes = notes
            }
            "rear_lights" -> {
                InspectionData.rearLights = status
                InspectionData.rearLightsNotes = notes
            }
            "side_lights" -> {
                InspectionData.sideLights = status
                InspectionData.sideLightsNotes = notes
            }
            "spare_wheel" -> {
                InspectionData.spareWheel = status
                InspectionData.spareWheelNotes = notes
            }
            "front_pads" -> {
                InspectionData.frontPads = status
                InspectionData.frontPadsNotes = notes
            }
            "rear_pads" -> {
                InspectionData.rearPads = status
                InspectionData.rearPadsNotes = notes
            }
            "front_disks" -> {
                InspectionData.frontDisks = status
                InspectionData.frontDisksNotes = notes
            }
            "rear_disks" -> {
                InspectionData.rearDisks = status
                InspectionData.rearDisksNotes = notes
            }
            "alignment" -> {
                InspectionData.alignment = status
                InspectionData.alignmentNotes = notes
            }
        }

        // Navigate to next step
        navigateToNextStep()
    }

    private fun navigateToNextStep() {
        val nextStepInfo = getNextStep(currentStep)

        if (nextStepInfo == null) {
            // Last step - go to review
            val intent = Intent(this, InspectionReviewActivity::class.java)
            startActivity(intent)
        } else {
            val (nextStep, nextStepNumber) = nextStepInfo
            val intent = Intent(this, when (nextStep) {
                "driver_front_tyre", "driver_rear_tyre", "passenger_front_tyre", "passenger_rear_tyre" -> TyreInspectionActivity::class.java
                else -> InspectionStepActivity::class.java
            })
            intent.putExtra("step", nextStep)
            intent.putExtra("stepNumber", nextStepNumber)
            intent.putExtra("totalSteps", totalSteps)
            startActivity(intent)
        }
    }

    private fun getNextStep(current: String): Pair<String, Int>? {
        return when (current) {
            "wipers" -> Pair("horn", 2)
            "horn" -> Pair("front_lights", 3)
            "front_lights" -> Pair("rear_lights", 4)
            "rear_lights" -> Pair("side_lights", 5)
            "side_lights" -> Pair("driver_front_tyre", 6)
            "spare_wheel" -> Pair("front_pads", 10)
            "front_pads" -> Pair("rear_pads", 11)
            "rear_pads" -> Pair("front_disks", 12)
            "front_disks" -> Pair("rear_disks", 13)
            "rear_disks" -> Pair("alignment", 14)
            "alignment" -> null // Last step
            else -> null
        }
    }
}
