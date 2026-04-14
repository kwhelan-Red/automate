package com.automate.customerform

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("submit_customer.php")
    suspend fun submitCustomer(@Body customerData: CustomerData): Response<ApiResponse>
}
