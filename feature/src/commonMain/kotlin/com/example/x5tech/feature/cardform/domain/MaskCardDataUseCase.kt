package com.example.x5tech.feature.cardform.domain

class MaskCardDataUseCase {

    fun maskCardNumber(input: String): String {
        val digits = input.filter(Char::isDigit)
        if (digits.isEmpty()) {
            return ""
        }

        if (digits.length <= VISIBLE_CARD_NUMBER_LENGTH) {
            return digits.chunked(CARD_NUMBER_GROUP_SIZE).joinToString(separator = " ")
        }

        val maskedDigits = buildString {
            append(digits.take(CARD_NUMBER_PREFIX_LENGTH))
            append(MASK_SYMBOL.toString().repeat(digits.length - VISIBLE_CARD_NUMBER_LENGTH))
            append(digits.takeLast(CARD_NUMBER_SUFFIX_LENGTH))
        }

        return maskedDigits.chunked(CARD_NUMBER_GROUP_SIZE).joinToString(separator = " ")
    }

    fun maskCardHolderName(input: String): String {
        val formattedName = input
            .uppercase()
            .trim()
            .replace(MULTIPLE_SPACES_REGEX, SINGLE_SPACE)

        if (formattedName.isEmpty()) {
            return ""
        }

        return formattedName
            .split(SINGLE_SPACE)
            .joinToString(separator = SINGLE_SPACE, transform = ::maskNamePart)
    }

    fun maskCvv(@Suppress("UNUSED_PARAMETER") input: String): String = CVV_MASK

    private fun maskNamePart(namePart: String): String {
        return when (namePart.length) {
            0 -> ""
            1 -> MASK_SYMBOL.toString()
            2 -> "${namePart.first()}$MASK_SYMBOL"
            else -> buildString {
                append(namePart.first())
                append(MASK_SYMBOL.toString().repeat(namePart.length - 2))
                append(namePart.last())
            }
        }
    }

    private companion object {
        const val CARD_NUMBER_PREFIX_LENGTH = 6
        const val CARD_NUMBER_SUFFIX_LENGTH = 4
        const val VISIBLE_CARD_NUMBER_LENGTH = CARD_NUMBER_PREFIX_LENGTH + CARD_NUMBER_SUFFIX_LENGTH
        const val CARD_NUMBER_GROUP_SIZE = 4
        const val SINGLE_SPACE = " "
        const val CVV_MASK = "•••"
        const val MASK_SYMBOL = '•'

        val MULTIPLE_SPACES_REGEX = Regex("\\s+")
    }
}
