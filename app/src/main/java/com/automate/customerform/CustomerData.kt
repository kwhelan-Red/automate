package com.automate.customerform

import com.google.gson.annotations.SerializedName

data class CustomerData(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("company")
    val company: String,

    @SerializedName("notes")
    val notes: String
)

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("customer_id")
    val customerId: Int? = null
)
