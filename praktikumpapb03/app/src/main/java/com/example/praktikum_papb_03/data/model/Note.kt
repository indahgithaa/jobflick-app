package com.example.praktikum_papb_03.data.model

import com.google.firebase.Timestamp

data class Note(
    val id: String? = null,
    val title: String = "",
    val content: String = "",
    val timestamp: Timestamp? = null
)
