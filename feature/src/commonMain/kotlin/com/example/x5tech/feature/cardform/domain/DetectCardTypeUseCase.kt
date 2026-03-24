package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardType

internal class DetectCardTypeUseCase {

    operator fun invoke(input: String): CardType {
        val digits = input.filter(Char::isDigit)

        return when {
            digits.startsWith(VISA_PREFIX) -> CardType.VISA
            matchesMastercard(digits) -> CardType.MASTERCARD
            matchesMir(digits) -> CardType.MIR
            else -> CardType.UNKNOWN
        }
    }

    private fun matchesMastercard(cardNumber: String): Boolean {
        val firstTwoDigits = cardNumber.take(TWO_DIGITS_LENGTH).toIntOrNull()
        if (firstTwoDigits in MASTERCARD_TWO_DIGITS_RANGE) {
            return true
        }

        val firstFourDigits = cardNumber.take(FOUR_DIGITS_LENGTH).toIntOrNull()
        return firstFourDigits in MASTERCARD_FOUR_DIGITS_RANGE
    }

    private fun matchesMir(cardNumber: String): Boolean {
        val firstFourDigits = cardNumber.take(FOUR_DIGITS_LENGTH).toIntOrNull()
        return firstFourDigits in MIR_FOUR_DIGITS_RANGE
    }

    private companion object {
        const val TWO_DIGITS_LENGTH = 2
        const val FOUR_DIGITS_LENGTH = 4
        const val VISA_PREFIX = "4"

        val MASTERCARD_TWO_DIGITS_RANGE = 51..55
        val MASTERCARD_FOUR_DIGITS_RANGE = 2221..2720
        val MIR_FOUR_DIGITS_RANGE = 2200..2204
    }
}
