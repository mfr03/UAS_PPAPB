package com.example.uas_ppapb_v2.database.model

data class Account(
    val uid: String,
    val username: String,
    val email: String,
    val nim: String,
    val dateOfBirth: String,
    val type: String = "user",
)