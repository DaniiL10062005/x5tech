package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult

internal class ValidateCvvUseCase {

    operator fun invoke(input: String): CardValidationResult {
        if (!CVV_REGEX.matches(input)) {
            return CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            )
        }

        return CardValidationResult.Valid
    }

    private companion object {
        val CVV_REGEX = Regex("^\\d{3}$")
    }
}
