package com.example.locationreminder.domain

data class Reminder(
    val id: Int,
    var title: String = "",
    var description: String = "",
    var location: String = ""
)
