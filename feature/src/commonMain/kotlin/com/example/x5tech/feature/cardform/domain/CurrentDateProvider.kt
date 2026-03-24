package com.example.x5tech.feature.cardform.domain

interface CurrentDateProvider {
    fun getCurrentDate(): CurrentDate
}

data class CurrentDate(
    val month: Int,
    val year: Int,
)
