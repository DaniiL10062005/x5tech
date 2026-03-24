package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult

class ValidateCardHolderNameUseCase(
    private val formatCardHolderNameUseCase: FormatCardHolderNameUseCase = FormatCardHolderNameUseCase(),
) {

    operator fun invoke(input: String): CardValidationResult {
        val formattedName = formatCardHolderNameUseCase(input)
        if (!input.all { it.isLetter() || it.isWhitespace() }) {
            return invalidResult()
        }

        if (formattedName.length < MIN_CARD_HOLDER_NAME_LENGTH) {
            return invalidResult()
        }

        if (!CARD_HOLDER_NAME_REGEX.matches(formattedName)) {
            return invalidResult()
        }

        return CardValidationResult.Valid
    }

    private fun invalidResult(): CardValidationResult.Invalid =
        CardValidationResult.Invalid(
            errors = listOf(CardValidationError.INVALID_HOLDER_NAME),
        )

    private companion object {
        const val MIN_CARD_HOLDER_NAME_LENGTH = 2
        val CARD_HOLDER_NAME_REGEX = Regex("^[A-Z ]+$")
    }
}
