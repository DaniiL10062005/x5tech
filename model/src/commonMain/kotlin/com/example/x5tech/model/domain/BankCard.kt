package com.example.x5tech.model.domain

data class BankCard(
    val number: String,
    val holderName: String,
    val expiryDate: String,
    val cvv: String,
    val type: CardType,
)
