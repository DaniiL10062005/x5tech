package com.example.x5tech.model.repository

import com.example.x5tech.model.domain.BankCard
import kotlinx.coroutines.delay

class FakeCardRepository(
    private val delayMillis: Long = DEFAULT_DELAY_MILLIS,
    private val shouldFail: Boolean = false,
    private val errorMessage: String = DEFAULT_ERROR_MESSAGE,
) : CardRepository {

    private val savedCards = mutableListOf<BankCard>()

    override suspend fun getBankByBin(bin: String): String? {
        delay(delayMillis)

        return LocalBankByBinResolver.resolve(bin)
    }

    override suspend fun saveCard(card: BankCard) {
        delay(delayMillis)

        if (shouldFail) {
            throw IllegalStateException(errorMessage)
        }

        savedCards += card
    }

    fun getSavedCards(): List<BankCard> = savedCards.toList()

    private companion object {
        const val DEFAULT_DELAY_MILLIS = 500L
        const val DEFAULT_ERROR_MESSAGE = "Failed to save card"
    }
}
