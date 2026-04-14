package com.automate.customerform

import com.automate.customerform.models.MechanicsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("submit_customer.php")
    suspend fun submitCustomer(@Body customerData: CustomerData): Response<ApiResponse>

    @GET("api_mechanics.php")
    suspend fun getMechanics(
        @Query("action") action: String = "list",
        @Query("active_only") activeOnly: Boolean = true
    ): Response<MechanicsResponse>
}
