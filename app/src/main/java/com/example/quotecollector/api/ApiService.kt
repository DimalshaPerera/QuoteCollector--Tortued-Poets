
package com.example.quotecollector.api

import com.example.quotecollector.models.Quote
import com.example.quotecollector.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users")
    suspend fun register(@Body user: User): Response<User>

    @GET("users")
    suspend fun login(@Query("email") email: String): Response<List<User>>

    @GET("users")
    suspend fun checkEmailExists(@Query("email") email: String): Response<List<User>>

    @GET("quotes")
    suspend fun getAllQuotes(): Response<List<Quote>>

    @GET("quotes")
    suspend fun getQuotesByUser(@Query("userId") userId: String): Response<List<Quote>>

    @GET("quotes")
    suspend fun searchQuotes(@Query("text") searchText: String): Response<List<Quote>>

    @GET("quotes")
    suspend fun filterQuotesByCategory(@Query("category") category: String): Response<List<Quote>>

    @POST("quotes")
    suspend fun createQuote(@Body quote: Quote): Response<Quote>

    @PUT("quotes/{id}")
    suspend fun updateQuote(@Path("id") id: String, @Body quote: Quote): Response<Quote>

    @DELETE("quotes/{id}")
    suspend fun deleteQuote(@Path("id") id: String): Response<Unit>

    @DELETE("quotes")
    suspend fun deleteAllQuotes(@Query("userId") userId: String): Response<Unit>
}