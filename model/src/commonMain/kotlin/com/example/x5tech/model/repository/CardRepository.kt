package com.example.x5tech.model.repository

import com.example.x5tech.model.domain.BankCard

interface CardRepository {
    suspend fun saveCard(card: BankCard)
}
