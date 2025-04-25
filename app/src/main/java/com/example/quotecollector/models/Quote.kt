package com.example.quotecollector.models

data class Quote(
    val id: String = "",
    val content: String,
    val author: String,
    val categories: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)