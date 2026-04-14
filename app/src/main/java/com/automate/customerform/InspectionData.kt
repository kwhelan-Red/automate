package com.automate.customerform

/**
 * Singleton to hold current inspection data across activities
 */
object InspectionData {
    // Vehicle Info
    var vehicleReg: String = ""
    var nctDate: String? = null
    var mileage: Int = 0
    var fitterName: String = ""
    var mechanicName: String? = null

    // Basic Checks
    var wipers: String = "pass"
    var wipersNotes: String = ""

    var horn: String = "pass"
    var hornNotes: String = ""

    // Lights
    var frontLights: String = "pass"
    var frontLightsNotes: String = ""

    var rearLights: String = "pass"
    var rearLightsNotes: String = ""

    var sideLights: String = "pass"
    var sideLightsNotes: String = ""

    // Tyres
    var driverFrontTread: Float = 0f
    var driverFrontPressure: Int = 0
    var driverFrontCondition: String = "good"
    var driverFrontNotes: String = ""

    var driverRearTread: Float = 0f
    var driverRearPressure: Int = 0
    var driverRearCondition: String = "good"
    var driverRearNotes: String = ""

    var passengerFrontTread: Float = 0f
    var passengerFrontPressure: Int = 0
    var passengerFrontCondition: String = "good"
    var passengerFrontNotes: String = ""

    var passengerRearTread: Float = 0f
    var passengerRearPressure: Int = 0
    var passengerRearCondition: String = "good"
    var passengerRearNotes: String = ""

    var spareWheel: String = "pass"
    var spareWheelNotes: String = ""

    // Brakes
    var frontPads: String = "pass"
    var frontPadsNotes: String = ""

    var rearPads: String = "pass"
    var rearPadsNotes: String = ""

    var frontDisks: String = "pass"
    var frontDisksNotes: String = ""

    var rearDisks: String = "pass"
    var rearDisksNotes: String = ""

    // Alignment
    var alignment: String = "pass"
    var alignmentNotes: String = ""

    // Overall
    var generalNotes: String = ""

    /**
     * Reset all data for a new inspection
     */
    fun reset() {
        vehicleReg = ""
        nctDate = null
        mileage = 0
        fitterName = ""
        mechanicName = null

        wipers = "pass"
        wipersNotes = ""
        horn = "pass"
        hornNotes = ""

        frontLights = "pass"
        frontLightsNotes = ""
        rearLights = "pass"
        rearLightsNotes = ""
        sideLights = "pass"
        sideLightsNotes = ""

        driverFrontTread = 0f
        driverFrontPressure = 0
        driverFrontCondition = "good"
        driverFrontNotes = ""

        driverRearTread = 0f
        driverRearPressure = 0
        driverRearCondition = "good"
        driverRearNotes = ""

        passengerFrontTread = 0f
        passengerFrontPressure = 0
        passengerFrontCondition = "good"
        passengerFrontNotes = ""

        passengerRearTread = 0f
        passengerRearPressure = 0
        passengerRearCondition = "good"
        passengerRearNotes = ""

        spareWheel = "pass"
        spareWheelNotes = ""

        frontPads = "pass"
        frontPadsNotes = ""
        rearPads = "pass"
        rearPadsNotes = ""
        frontDisks = "pass"
        frontDisksNotes = ""
        rearDisks = "pass"
        rearDisksNotes = ""

        alignment = "pass"
        alignmentNotes = ""
        generalNotes = ""
    }

    /**
     * Calculate overall status based on all checks
     */
    fun getOverallStatus(): String {
        val allChecks = listOf(
            wipers, horn, frontLights, rearLights, sideLights,
            driverFrontCondition, driverRearCondition,
            passengerFrontCondition, passengerRearCondition,
            spareWheel, frontPads, rearPads, frontDisks, rearDisks, alignment
        )

        return when {
            allChecks.any { it == "fail" } -> "fail"
            allChecks.any { it == "advisory" || it == "worn" || it == "replace" } -> "advisory"
            else -> "pass"
        }
    }
}
