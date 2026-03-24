package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult

internal class ValidateExpiryDateUseCase(
    private val currentDateProvider: CurrentDateProvider,
) {

    operator fun invoke(input: String): CardValidationResult {
        val matchResult = EXPIRY_DATE_REGEX.matchEntire(input) ?: return invalidResult()

        val month = matchResult.groupValues[MONTH_GROUP_INDEX].toInt()
        val year = matchResult.groupValues[YEAR_GROUP_INDEX].toInt()
        val currentDate = currentDateProvider.getCurrentDate()

        if (month !in VALID_MONTH_RANGE) {
            return invalidResult()
        }

        val isInPast = year < currentDate.year ||
            (year == currentDate.year && month < currentDate.month)

        if (isInPast) {
            return invalidResult()
        }

        return CardValidationResult.Valid
    }

    private fun invalidResult(): CardValidationResult.Invalid =
        CardValidationResult.Invalid(
            errors = listOf(CardValidationError.INVALID_EXPIRATION_DATE),
        )

    private companion object {
        const val MONTH_GROUP_INDEX = 1
        const val YEAR_GROUP_INDEX = 2

        val EXPIRY_DATE_REGEX = Regex("^(\\d{2})/(\\d{2})$")
        val VALID_MONTH_RANGE = 1..12
    }
}
