package com.example.quotecollector.api

import com.example.quotecollector.models.RegisterRequest
import com.example.quotecollector.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}