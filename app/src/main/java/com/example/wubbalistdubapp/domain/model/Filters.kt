package com.example.wubbalistdubapp.domain.model

data class Filters(
    val name: String = "",
    val status: String = "any",
    val gender: String = "any"
) {
    fun isDefault() = name.isBlank() && status == "any" && gender == "any"
}
