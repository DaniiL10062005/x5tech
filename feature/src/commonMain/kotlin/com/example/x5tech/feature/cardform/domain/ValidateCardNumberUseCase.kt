package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult

class ValidateCardNumberUseCase {

    operator fun invoke(input: String): CardValidationResult {
        if (input.any { !it.isDigit() && !it.isWhitespace() }) {
            return invalidResult()
        }

        val digits = input.filter(Char::isDigit)
        if (digits.length != CARD_NUMBER_LENGTH) {
            return invalidResult()
        }

        if (!passesLuhn(digits)) {
            return invalidResult()
        }

        return CardValidationResult.Valid
    }

    private fun passesLuhn(cardNumber: String): Boolean {
        val checksum = cardNumber
            .reversed()
            .mapIndexed { index, digit ->
                val number = digit.digitToInt()
                if (index % 2 == 1) {
                    val doubled = number * 2
                    if (doubled > 9) doubled - 9 else doubled
                } else {
                    number
                }
            }
            .sum()

        return checksum % 10 == 0
    }

    private fun invalidResult(): CardValidationResult.Invalid =
        CardValidationResult.Invalid(
            errors = listOf(CardValidationError.INVALID_NUMBER),
        )

    private companion object {
        const val CARD_NUMBER_LENGTH = 16
    }
}
