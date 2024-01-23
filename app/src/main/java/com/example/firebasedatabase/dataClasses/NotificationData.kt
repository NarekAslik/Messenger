package com.example.firebasedatabase.dataClasses

import java.io.Serializable

data class NotificationData(
    val title: String,
    val text: String,
    val sender: String
) : Serializable
