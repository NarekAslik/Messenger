package com.example.firebasedatabase.dataClasses

import java.io.Serializable

data class PersonInfo(
    val name: String,
    val lastname: String,
    val surname: String
) : Serializable
