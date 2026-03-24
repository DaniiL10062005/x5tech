package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidateCvvUseCaseTest {

    private val validateCvvUseCase = ValidateCvvUseCase()

    @Test
    fun `returns valid for three digit cvv`() {
        val result = validateCvvUseCase("123")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns invalid for short cvv`() {
        val result = validateCvvUseCase("12")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for long cvv`() {
        val result = validateCvvUseCase("1234")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for cvv with letters`() {
        val result = validateCvvUseCase("1a3")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }
}
