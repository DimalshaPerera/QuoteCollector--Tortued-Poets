package com.example.quotecollector.models

data class Quote(
    val id: String = "",
    val text: String,
    val author: String,
    val category: String,
    val userId: String,
    val createdAt: String = System.currentTimeMillis().toString()
)