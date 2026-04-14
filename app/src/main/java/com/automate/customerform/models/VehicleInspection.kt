package com.automate.customerform.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class VehicleInspection(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("vehicle_reg")
    val vehicleReg: String,

    @SerializedName("nct_date")
    val nctDate: String?,

    @SerializedName("mileage")
    val mileage: Int,

    @SerializedName("fitter_name")
    val fitterName: String,

    @SerializedName("mechanic_name")
    val mechanicName: String?,

    // Inspection Items
    @SerializedName("wipers")
    val wipers: String, // pass/fail/advisory

    @SerializedName("horn")
    val horn: String,

    @SerializedName("front_lights")
    val frontLights: String,

    @SerializedName("rear_lights")
    val rearLights: String,

    @SerializedName("side_lights")
    val sideLights: String,

    // Tyres
    @SerializedName("driver_front_tyre")
    val driverFrontTyre: TyreInspection,

    @SerializedName("driver_rear_tyre")
    val driverRearTyre: TyreInspection,

    @SerializedName("passenger_front_tyre")
    val passengerFrontTyre: TyreInspection,

    @SerializedName("passenger_rear_tyre")
    val passengerRearTyre: TyreInspection,

    @SerializedName("spare_wheel")
    val spareWheel: String,

    // Brakes
    @SerializedName("front_pads")
    val frontPads: String,

    @SerializedName("rear_pads")
    val rearPads: String,

    @SerializedName("front_disks")
    val frontDisks: String,

    @SerializedName("rear_disks")
    val rearDisks: String,

    // Alignment
    @SerializedName("alignment")
    val alignment: String,

    @SerializedName("notes")
    val notes: String?,

    @SerializedName("status")
    val status: String, // pending/completed/submitted

    @SerializedName("created_at")
    val createdAt: String?
)

data class TyreInspection(
    @SerializedName("tread_depth")
    val treadDepth: Float, // in mm

    @SerializedName("pressure")
    val pressure: Int, // in PSI

    @SerializedName("condition")
    val condition: String, // good/worn/replace

    @SerializedName("notes")
    val notes: String?
)

data class InspectionListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("inspections")
    val inspections: List<VehicleInspection>,

    @SerializedName("message")
    val message: String?
)

data class InspectionSubmitResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("inspection_id")
    val inspectionId: Int?,

    @SerializedName("message")
    val message: String?
)
