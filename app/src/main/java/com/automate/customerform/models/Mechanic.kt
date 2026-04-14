package com.automate.customerform.models

import com.google.gson.annotations.SerializedName

data class Mechanic(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("role")
    val role: String,

    @SerializedName("active")
    val active: String
)

data class MechanicsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("mechanics")
    val mechanics: List<Mechanic>,

    @SerializedName("count")
    val count: Int
)
