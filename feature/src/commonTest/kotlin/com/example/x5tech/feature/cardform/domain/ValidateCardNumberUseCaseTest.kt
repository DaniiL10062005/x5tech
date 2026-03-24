package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidateCardNumberUseCaseTest {

    private val validateCardNumberUseCase = ValidateCardNumberUseCase()

    @Test
    fun `returns valid for visa test card number`() {
        val result = validateCardNumberUseCase("4111 1111 1111 1111")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns valid for mastercard test card number`() {
        val result = validateCardNumberUseCase("5555 5555 5555 4444")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns valid for mir test card number`() {
        val result = validateCardNumberUseCase("2200 0000 0000 0004")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns invalid for card number that fails luhn check`() {
        val result = validateCardNumberUseCase("4111 1111 1111 1112")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_NUMBER),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for short card number`() {
        val result = validateCardNumberUseCase("4111 1111 1111")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_NUMBER),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for card number with letters`() {
        val result = validateCardNumberUseCase("4111 1111 1111 11ab")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_NUMBER),
            ),
            result,
        )
    }
}
