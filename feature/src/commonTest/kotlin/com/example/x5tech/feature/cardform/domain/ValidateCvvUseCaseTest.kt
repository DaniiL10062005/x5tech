package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidateCvvUseCaseTest {

    private val validateCvvUseCase = ValidateCvvUseCase()

    @Test
    fun `should validate CVV length`() {
        val result = validateCvvUseCase("123")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `should reject short CVV`() {
        val result = validateCvvUseCase("12")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }

    @Test
    fun `should reject long CVV`() {
        val result = validateCvvUseCase("1234")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }

    @Test
    fun `should reject non digit CVV`() {
        val result = validateCvvUseCase("1a3")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_CVV),
            ),
            result,
        )
    }
}
