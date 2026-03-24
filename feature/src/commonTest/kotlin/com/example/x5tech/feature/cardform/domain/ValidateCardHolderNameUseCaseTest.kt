package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidateCardHolderNameUseCaseTest {

    private val formatCardHolderNameUseCase = FormatCardHolderNameUseCase()
    private val validateCardHolderNameUseCase = ValidateCardHolderNameUseCase(
        formatCardHolderNameUseCase = formatCardHolderNameUseCase,
    )

    @Test
    fun `returns valid for uppercase latin name`() {
        val result = validateCardHolderNameUseCase("JOHN SMITH")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns invalid for too short name`() {
        val result = validateCardHolderNameUseCase("J")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_HOLDER_NAME),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for cyrillic name`() {
        val result = validateCardHolderNameUseCase("ИВАН ИВАНОВ")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_HOLDER_NAME),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for name with digits`() {
        val result = validateCardHolderNameUseCase("JOHN2 SMITH")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_HOLDER_NAME),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for name with special characters`() {
        val result = validateCardHolderNameUseCase("JOHN@SMITH")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_HOLDER_NAME),
            ),
            result,
        )
    }

    @Test
    fun `formats lowercase name with extra spaces to uppercase`() {
        val formattedName = formatCardHolderNameUseCase("  john   smith  ")

        assertEquals("JOHN SMITH", formattedName)
    }
}
