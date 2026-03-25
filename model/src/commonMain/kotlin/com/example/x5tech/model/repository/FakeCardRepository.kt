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

        val normalizedBin = bin.take(BIN_LENGTH).toIntOrNull() ?: return null

        return bankRanges.firstNotNullOfOrNull { bankRange ->
            bankRange.bankName.takeIf { normalizedBin in bankRange.range }
        }
    }

    override suspend fun saveCard(card: BankCard) {
        delay(delayMillis)

        if (shouldFail) {
            throw IllegalStateException(errorMessage)
        }

        savedCards += card
    }

    fun getSavedCards(): List<BankCard> = savedCards.toList()

    private data class BankRange(
        val range: IntRange,
        val bankName: String,
    )

    private companion object {
        const val DEFAULT_DELAY_MILLIS = 500L
        const val DEFAULT_ERROR_MESSAGE = "Failed to save card"
        const val BIN_LENGTH = 6

        val bankRanges = listOf(
            BankRange(range = 427600..427699, bankName = "SberBank"),
            BankRange(range = 546900..546999, bankName = "SberBank"),
            BankRange(range = 220200..220299, bankName = "SberBank"),
            BankRange(range = 521300..521399, bankName = "T-Bank"),
            BankRange(range = 437700..437799, bankName = "T-Bank"),
            BankRange(range = 553690..553699, bankName = "T-Bank"),
            BankRange(range = 458440..458449, bankName = "Alfa-Bank"),
            BankRange(range = 548670..548679, bankName = "Alfa-Bank"),
            BankRange(range = 427220..427239, bankName = "VTB"),
            BankRange(range = 462230..462239, bankName = "VTB"),
            BankRange(range = 220020..220029, bankName = "VTB"),
            BankRange(range = 427480..427499, bankName = "Gazprombank"),
            BankRange(range = 548990..548999, bankName = "Gazprombank"),
        )
    }
}
