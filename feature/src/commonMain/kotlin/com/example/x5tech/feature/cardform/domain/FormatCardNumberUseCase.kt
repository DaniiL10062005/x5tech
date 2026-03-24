package com.example.x5tech.feature.cardform.domain

class FormatCardNumberUseCase {

    operator fun invoke(input: String): String {
        val digits = input
            .filter(Char::isDigit)
            .take(MAX_CARD_NUMBER_LENGTH)

        return digits.chunked(CARD_NUMBER_GROUP_SIZE).joinToString(separator = " ")
    }

    private companion object {
        const val MAX_CARD_NUMBER_LENGTH = 16
        const val CARD_NUMBER_GROUP_SIZE = 4
    }
}
