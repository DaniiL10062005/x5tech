package com.example.x5tech.feature.cardform.domain

internal class FormatCardHolderNameUseCase {

    operator fun invoke(input: String): String {
        return input
            .uppercase()
            .replace(MULTIPLE_SPACES_REGEX, SINGLE_SPACE)
            .trim()
    }

    private companion object {
        val MULTIPLE_SPACES_REGEX = Regex("\\s+")
        const val SINGLE_SPACE = " "
    }
}
