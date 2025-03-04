package com.example.cashiq.model

data class NoteModel(
    val id: String = "",         // Primary Key
    val comment: String = "",    // Note or comment
    val userId: String = ""      // Foreign Key (User ID)
)
